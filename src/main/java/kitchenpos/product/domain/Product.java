package kitchenpos.product.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.product.dto.ProductRequest;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class Product {
    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;

    @Embedded
    private Price price;

    public Product() {
    }

    public Product(ProductRequest productRequest) {
        this(productRequest.getName(), productRequest.getPrice());
    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = new Price(price);
    }

    public Long id() {
        return id;
    }

    public String name() {
        return name;
    }

    public BigDecimal price() {
        return price.price();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(id, product.id) && Objects.equals(name, product.name) && Objects.equals(price, product.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
    }
}
