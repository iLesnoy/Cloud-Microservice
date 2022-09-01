package com.petrovskiy.epm.model;

import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    @Column(name = "id", unique = true, nullable = false, updatable = false)
    private Long id;

    @Column(name = "order_date", nullable = false, updatable = false)
    private LocalDateTime purchaseTime;

    @Column(name = "order_cost", nullable = false, updatable = false)
    private BigDecimal cost;

    @ManyToOne
    @JoinColumn(name = "users_id")
    private User user;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(name="gift_certificate_has_orders"
            ,joinColumns = @JoinColumn(name = "orders_id", referencedColumnName = "id")
            ,inverseJoinColumns = @JoinColumn(name = "gift_certificate_id",referencedColumnName = "id"))
    private List<GiftCertificate> certificateList = new ArrayList<>();


    @PrePersist
    private void prePersist(){
        purchaseTime = LocalDateTime.now();
        cost =  certificateList.stream().map(GiftCertificate::getPrice).reduce(BigDecimal.ONE,BigDecimal::add);
    }
}
