package br.com.oficinapro.produto.domain;

import br.com.oficinapro.categoriaProduto.domain.ProductCategory;
import br.com.oficinapro.common.audit.entity.BaseEntity;
import br.com.oficinapro.fornecedor.domain.Supplier;
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

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Audited
@Entity
@Table(name = "tb_product")
public class Product extends BaseEntity {

    @Column(nullable = false)
    private String name;

    @Column(nullable = false, unique = true)
    private String sku;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private ProductCategory category;

    @Column(nullable = false)
    private String unit;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal costPrice;

    @Column(nullable = false, precision = 19, scale = 2)
    private BigDecimal salePrice;

    @Column(nullable = false)
    private Integer currentStock;

    @Column(nullable = false)
    private Integer minimumStock;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supplier_id")
    private Supplier supplier;

    @Column(nullable = false)
    private boolean enabled = true;
}
