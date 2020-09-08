package com.urequest.service;

import com.google.gson.Gson;
import com.google.gson.JsonParseException;
import com.urequest.domain.Customer;
import com.urequest.dto.ProcessRequestV1;
import com.urequest.dto.ProcessResponseV1;
import com.urequest.repository.CustomerRepository;
import com.urequest.repository.IpBlacklistRepository;
import com.urequest.repository.UserAgentBlacklistRepository;
import com.urequest.service.interfaces.RequestService;
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

    @Autowired
    public RequestServiceImpl(CustomerRepository customerRepository, UserAgentBlacklistRepository userAgentBlacklistRepository, IpBlacklistRepository ipBlacklistRepository) {
        this.customerRepository = customerRepository;
        this.userAgentBlacklistRepository = userAgentBlacklistRepository;
        this.ipBlacklistRepository = ipBlacklistRepository;
    }

    @Override
    public ProcessResponseV1 process(String userAgent, String request) {
        Optional<ProcessRequestV1> processRequest = parseRequest(request);
        boolean isValid = processRequest.isPresent() && isValid(userAgent, processRequest.get());
        if (isValid) {
            return processValidRequest();
        }
        return processInvalidRequest();
    }

    private Optional<ProcessRequestV1> parseRequest(String request) {
        try {
            return Optional.of(new Gson().fromJson(request, ProcessRequestV1.class));
        } catch(JsonParseException ignored) { }
        return Optional.empty();
    }

    private void doSomething() {
    }

    private void emitValidRequestEvent() {
    }

    private void emitValidationErrorEvent() {
    }

    private ProcessResponseV1 processValidRequest() {
        emitValidRequestEvent();
        doSomething();
        return new ProcessResponseV1(HttpStatus.OK);
    }

    private ProcessResponseV1 processInvalidRequest() {
        emitValidationErrorEvent();
        return new ProcessResponseV1(HttpStatus.BAD_REQUEST);
    }

    private boolean isValid(String userAgent, ProcessRequestV1 request) {
        return isRequestWellformed(request) && isValidIpAddress(request) &&
                isValidUserAgent(userAgent) && isValidCustomer(request);
    }

    private boolean isRequestWellformed(ProcessRequestV1 request) {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        Validator validator = factory.getValidator();

        return validator.validate(request).isEmpty();
    }

    private boolean isValidCustomer(ProcessRequestV1 request) {
        Optional<Customer> customer = customerRepository.findById(request.getCustomerId());
        return customer.isPresent() && customer.get().isActive();
    }

    private boolean isValidUserAgent(String userAgent) {
        return userAgentBlacklistRepository.findByUserAgent(userAgent).isEmpty();
    }

    private boolean isValidIpAddress(ProcessRequestV1 request) {
        return ipBlacklistRepository.findByIp(request.getRemoteIp()).isEmpty();
    }
}
