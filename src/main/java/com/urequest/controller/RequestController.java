package com.urequest.controller;

import com.urequest.dto.ProcessRequestV1;
import com.urequest.dto.ProcessResponseV1;
import com.urequest.service.interfaces.RequestService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class RequestController {

    private final
    RequestService requestService;

    private final Logger log = LoggerFactory.getLogger(getClass());

    @Autowired
    public RequestController(RequestService requestService) {
        this.requestService = requestService;
    }

    @RequestMapping(path = "/v1/requests", method = RequestMethod.POST)
    public ResponseEntity<ProcessResponseV1> process(@RequestHeader(value = "User-Agent") String userAgent, @RequestBody String request) {
        try {
            ProcessResponseV1 response = requestService.process(userAgent, request);
            return new ResponseEntity<>(response, response.getStatus());
        } catch(Exception e) {
            log.error("Error processing request: " + request + ".", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
