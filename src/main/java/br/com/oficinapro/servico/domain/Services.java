package br.com.oficinapro.servico.domain;

import br.com.oficinapro.common.audit.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Entity
@Table(name = "tb_services")
public class Services extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String category;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal defaultPrice;

    @Column(nullable = false)
    private Integer estimatedTimeMinutes;

    @Column(nullable = false)
    private boolean enabled = true;
}
