package kitchenpos.product.domain;

import kitchenpos.common.Name;
import kitchenpos.common.Price;
import kitchenpos.product.dto.ProductRequest;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Product {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private Name name;
    @Embedded
    private Price price;

    public Product() {

    }

    public Product(String name, Price price) {
        this(null, new Name(name), price);
    }

    public Product(Long id, Name name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public static Product of(ProductRequest request) {
        return new Product(request.getName(), new Price(request.getPrice()));
    }

    public Long getId() {
        return id;
    }

    public Name getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product product = (Product) o;
        return Objects.equals(getId(), product.getId()) && Objects.equals(getName(), product.getName()) && Objects.equals(getPrice(), product.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getName(), getPrice());
    }
}
