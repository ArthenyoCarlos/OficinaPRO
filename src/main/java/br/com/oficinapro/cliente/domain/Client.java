package br.com.oficinapro.cliente.domain;

import br.com.oficinapro.common.audit.entity.BaseEntity;
import br.com.oficinapro.veiculo.domain.Vehicle;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.envers.Audited;

import java.util.HashSet;
import java.util.Set;

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

    @OneToMany(mappedBy = "client")
    private Set<Vehicle> vehicles = new HashSet<>();

    private boolean enabled;

    public void addVehicle(Vehicle vehicle) {
        vehicles.add(vehicle);
        vehicle.setClient(this);
    }

    public void removeVehicle(Vehicle vehicle) {
        vehicles.remove(vehicle);
        vehicle.setClient(null);
    }
}
