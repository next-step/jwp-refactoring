package kitchenpos.product.domain;

import javax.persistence.*;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(unique = true)
    private String name;
    @Embedded
    private ProductPrice price;

    public Product() {
    }

    public Product(String name, int price) {
        this.name = name;
        this.price = ProductPrice.from(price);
    }

    public Product(Long id, String name, int price) {
        this.id = id;
        this.name = name;
        this.price = ProductPrice.from(price);
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
