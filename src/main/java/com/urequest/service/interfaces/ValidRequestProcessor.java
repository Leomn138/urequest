package com.urequest.service.interfaces;

import com.urequest.dto.ValidatedRequest;

public interface ValidRequestProcessor {
    void process(ValidatedRequest validatedRequest);

    void emitRequestValidatedEvent(ValidatedRequest validatedRequest);
}
