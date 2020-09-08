package com.urequest.repository;

import com.urequest.domain.UserAgent;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserAgentBlacklistRepository extends CrudRepository<UserAgent, Integer> {
    Optional<UserAgent> findByUserAgent(String userAgent);
}

