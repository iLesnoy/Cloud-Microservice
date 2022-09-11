package com.petrovskiy.epm.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.Set;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "tags")
@Entity
public class Tag{

    @Id
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotEmpty
    @NotBlank(message = "tagName is mandatory")
    @Pattern(regexp = "^[\\p{Alpha}А-Яа-я]{4,40}$")
    @Column(name = "name", unique = true, nullable = false, updatable = false)
    private String name;

    @ManyToMany(fetch = FetchType.LAZY, mappedBy = "tagList")
    private Set<GiftCertificate> certificates;

}