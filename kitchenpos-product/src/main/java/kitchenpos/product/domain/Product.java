package kitchenpos.product.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "product")
public class
Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private ProductName name;
    @Column
    private Price price;

    private Product(ProductName name, Price price) {
        this.name = name;
        this.price = price;
    }

    private Product(Long id, ProductName name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    protected Product() {
    }

    public static Product of(ProductName name, Price price) {
        return new Product(name, price);
    }

    public static Product of(Long id, ProductName name, Price price) {
        return new Product(id, name, price);
    }

    public Long getId() {
        return id;
    }

    public ProductName getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Product product = (Product) o;
        return name.equals(product.name) && price.equals(product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, price);
    }
}
