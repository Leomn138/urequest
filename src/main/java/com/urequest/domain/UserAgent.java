package com.urequest.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@Entity
@Table(name = "ua_blacklist")
public class UserAgent {
    @Id
    private Integer id;

    @NotNull
    @Column
    private String userAgent;
}
