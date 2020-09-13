package com.urequest.service;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.urequest.domain.Customer;
import com.urequest.dto.ProcessRequest;
import com.urequest.dto.ProcessResponseV1;
import com.urequest.dto.ValidatedRequest;
import com.urequest.repository.CustomerRepository;
import com.urequest.repository.IpBlacklistRepository;
import com.urequest.repository.UserAgentBlacklistRepository;
import com.urequest.service.interfaces.RequestService;
import com.urequest.service.interfaces.ValidRequestProcessor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import java.util.Optional;

@Service
public class RequestServiceImpl implements RequestService {

    private final
    CustomerRepository customerRepository;

    private final
    UserAgentBlacklistRepository userAgentBlacklistRepository;

    private final
    IpBlacklistRepository ipBlacklistRepository;

    private final ValidRequestProcessor validRequestProcessor;

    @Autowired
    public RequestServiceImpl(CustomerRepository customerRepository, UserAgentBlacklistRepository userAgentBlacklistRepository, IpBlacklistRepository ipBlacklistRepository, ValidRequestProcessor validRequestProcessor) {
        this.customerRepository = customerRepository;
        this.userAgentBlacklistRepository = userAgentBlacklistRepository;
        this.ipBlacklistRepository = ipBlacklistRepository;
        this.validRequestProcessor = validRequestProcessor;
    }

    @Override
    public ProcessResponseV1 process(String userAgent, String request) {
        Optional<ProcessRequest> processRequest = parseRequest(request);
        ValidatedRequest validatedRequest = ValidatedRequest.of(processRequest, isValid(userAgent, processRequest));
        if (validatedRequest.isValid()) {
            validRequestProcessor.process(validatedRequest);
        }
        validRequestProcessor.emitRequestValidatedEvent(validatedRequest);
        return fillProcessResponse(validatedRequest.isValid());
    }

    private ProcessResponseV1 fillProcessResponse(boolean isValid) {
        if (isValid) {
            return new ProcessResponseV1(HttpStatus.OK);
        }
        return new ProcessResponseV1(HttpStatus.BAD_REQUEST);
    }

    private Optional<ProcessRequest> parseRequest(String request) {
        try {
            return Optional.of(new Gson().fromJson(request, ProcessRequest.class));
        } catch(JsonParseException ignored) { }
        return Optional.empty();
    }

    private boolean isValid(String userAgent, Optional<ProcessRequest> request) {
        return request.isPresent() && isRequestWellformed(request.get()) && isValidIpAddress(request.get()) &&
                isValidUserAgent(userAgent) && isValidCustomer(request.get());
    }

    private boolean isRequestWellformed(ProcessRequest request) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(request).isEmpty();
    }

    private boolean isValidCustomer(ProcessRequest request) {
        Optional<Customer> customer = customerRepository.findById(request.getCustomerId());
        return customer.isPresent() && customer.get().isActive();
    }

    private boolean isValidUserAgent(String userAgent) {
        return userAgentBlacklistRepository.findByUserAgent(userAgent).isEmpty();
    }

    private boolean isValidIpAddress(ProcessRequest request) {
        return ipBlacklistRepository.findByIp(request.getRemoteIp()).isEmpty();
    }
}
