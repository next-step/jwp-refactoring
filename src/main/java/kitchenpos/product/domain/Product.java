package kitchenpos.product.domain;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private ProductPrice price;

    protected Product() {
    }

    public Product(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = ProductPrice.from(price);
    }

    private Product(String name, int price) {
        this.name = name;
        this.price = ProductPrice.from(price);
    }

    public static Product of(String name, int price) {
        return new Product(name, price);
    }

    public static Product of(Long id, String name, int price) {
        return new Product(id, name, price);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price.getValue();
    }
}
