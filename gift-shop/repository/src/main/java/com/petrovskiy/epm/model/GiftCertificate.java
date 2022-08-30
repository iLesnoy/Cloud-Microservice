package com.petrovskiy.epm.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "gift_certificate")
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;
    @Column(name = "name", unique = true, nullable = false)
    private String name;
    @Column(name = "description",nullable = false)
    private String description;
    @Column(name = "price",nullable = false)
    private BigDecimal price;
    @Column(name = "duration",nullable = false)
    private int duration;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinTable(name = "gift_certificate_has_tag"
            , joinColumns = @JoinColumn(name = "gift_certificate_id", referencedColumnName = "id")
            , inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tagList = new HashSet<>();

    @PrePersist
    private void PrePersist(){
        createDate = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        lastUpdateDate = createDate;
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdateDate = LocalDateTime.now();
    }
}