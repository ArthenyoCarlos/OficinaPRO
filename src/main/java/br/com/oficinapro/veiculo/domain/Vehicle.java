package br.com.oficinapro.veiculo.domain;

import br.com.oficinapro.common.audit.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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

    private boolean enabled = true;
}
