package br.com.oficinapro.fornecedor.domain;

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
@Table(name = "tb_supplier")
public class Supplier extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(unique = true, nullable = false)
    private String cnpj;

    private String phone;

    @Column(unique = true)
    private String email;

    private String contact;

    @Column(columnDefinition = "TEXT")
    private String observation;

    private boolean enabled = true;
}
