package br.com.oficinapro.cliente.domain;

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
@Table(name = "tb_client")
public class Client extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String cpfCnpj;

    private String phone;

    @Column(unique = true)
    private String email;

    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String observation;
    private boolean enabled;
}
