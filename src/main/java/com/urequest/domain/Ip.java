package com.urequest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Id;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "ip_blacklist")
public class Ip {

    @Id
    private Integer id;

    @Column
    private String ip;
}
