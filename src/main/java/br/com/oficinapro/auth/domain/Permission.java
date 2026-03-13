package br.com.oficinapro.auth.domain;

import br.com.oficinapro.common.audit.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Audited
@Entity
@Table(name = "tb_permission")
public class Permission extends BaseEntity implements GrantedAuthority {
    
    @Column(unique = true, nullable = false)
    private String name;

    @Column(nullable = false)
    private String description;

    @Override
    public String getAuthority() {
        return this.name;
    }
}
