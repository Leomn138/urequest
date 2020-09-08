package com.urequest.service.interfaces;

import com.urequest.dto.ProcessResponseV1;

public interface RequestService {
    ProcessResponseV1 process(String userAgent, String request);
}
