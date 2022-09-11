package com.petrovskiy.epm.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Set;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "gift_certificates")
public class GiftCertificate {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @NotEmpty(message = "Name is mandatory")
    @Pattern(regexp = "^[\\p{Alpha}А-Яа-я]{2,65}$")
    @Column(name = "name", unique = true, nullable = false)
    private String name;

    @Pattern(regexp = "[\\p{Alpha}А-Яа-я\\d-.,:;!?()\" ]{2,225}")
    @Column(name = "description",nullable = false)
    private String description;

    @NotNull
    /*@Pattern(regexp = "^(\\d+|[\\.\\,]?\\d+){1,2}$")*/
    @Column(name = "price",nullable = false)
    private BigDecimal price;

    @Column(name = "duration",nullable = false)
    private int duration;

    @Column(name = "create_date",nullable = false)
    private LocalDateTime createDate;
    @Column(name = "last_update_date")
    private LocalDateTime lastUpdateDate;

    @ManyToMany(fetch = FetchType.LAZY,cascade = CascadeType.PERSIST)
    @JoinTable(name = "gift_certificate_has_tag"
            , joinColumns = @JoinColumn(name = "gift_certificate_id", referencedColumnName = "id")
            , inverseJoinColumns = @JoinColumn(name = "tag_id", referencedColumnName = "id"))
    private Set<Tag> tagList;

    @PrePersist
    private void PrePersist(){
        createDate = LocalDateTime.parse(LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
        lastUpdateDate = createDate;
    }

    @PreUpdate
    public void preUpdate() {
        lastUpdateDate = LocalDateTime.now();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        GiftCertificate that = (GiftCertificate) o;
        return id != null && Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}