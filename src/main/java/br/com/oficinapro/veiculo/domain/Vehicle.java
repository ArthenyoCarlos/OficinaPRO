package br.com.oficinapro.veiculo.domain;

import br.com.oficinapro.common.audit.entity.BaseEntity;
import br.com.oficinapro.cliente.domain.Client;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Entity
@Table(name = "tb_vehicle")
public class Vehicle extends BaseEntity {

    @Column(nullable = false, unique = true)
    private String plate;

    private String chassis;

    private String renavam;

    private String brand;

    private String model;

    private Integer manufactureYear;

    private Integer modelYear;

    private String color;

    private String fuelType;

    @Column(nullable = false)
    private Long currentMileage;

    private String notes;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id")
    private Client client;

    private boolean enabled = true;
}
