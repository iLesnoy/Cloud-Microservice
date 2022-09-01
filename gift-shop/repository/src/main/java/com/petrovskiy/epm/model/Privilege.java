package com.petrovskiy.epm.model;

import lombok.Data;

import javax.persistence.*;
import java.util.Set;

@Data
@Entity
@Table(name = "privileges")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String name;

    /*@ManyToMany(mappedBy = "privilege",fetch = FetchType.LAZY)
    private Set<Role> roles;*/
}
