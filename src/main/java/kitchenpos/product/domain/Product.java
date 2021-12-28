package kitchenpos.product.domain;

import kitchenpos.domain.Name;
import kitchenpos.domain.Price;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Name name;

    @Embedded
    private Price price;

    protected Product() {
    }

    private Product(Long id, String name, Long price) {
        this.id = id;
        this.name = Name.of(name);
        this.price = Price.of(price);
    }

    public static Product of(Long id, String name, Long price) {
        return new Product(id, name, price);
    }

    public static Product of(String name, Long price) {
        return new Product(null, name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name.getName();
    }

    public Long getPrice() {
        return price.getPrice();
    }

    public Price multiply(Long quantity) {
        return price.multiply(quantity);
    }
}
