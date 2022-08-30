package com.petrovskiy.epm.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Table(name = "tag")
@Entity
public class Tag{

    @Id
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name", unique = true, nullable = false, updatable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tagList")
    private Set<GiftCertificate> certificates;

}