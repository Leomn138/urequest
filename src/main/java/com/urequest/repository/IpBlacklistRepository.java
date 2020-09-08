package com.urequest.repository;

import com.urequest.domain.Ip;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface IpBlacklistRepository extends CrudRepository<Ip, Integer> {
    Optional<Ip> findByIp(String ip);
}
