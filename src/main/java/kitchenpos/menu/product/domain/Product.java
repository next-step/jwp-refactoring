package kitchenpos.menu.product.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Table(name = "product")
@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    @Embedded
    private ProductPrice productPrice;

    public Product() {

    }

    private Product(String name, BigDecimal price) {
        this.name = name;
        this.productPrice = new ProductPrice(price);
    }

    public static Product create(String name, BigDecimal price) {
        return new Product(name, price);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public ProductPrice getPriceProduct() {
        return productPrice;
    }

    public void setPrice(final BigDecimal price) {
        this.productPrice = new ProductPrice(price);
    }

    public BigDecimal multiplyPrice(BigDecimal valueOf) {
        return null;
    }
}
