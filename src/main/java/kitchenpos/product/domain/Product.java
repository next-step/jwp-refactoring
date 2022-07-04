package kitchenpos.product.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    @Embedded
    private ProductPrice price;

    public Product() {
    }

    public Product(Long id, String name, long price) {
        this.id = id;
        this.name = name;
        this.price = ProductPrice.from(price);
    }

    public Product(String name, long price) {
        this.name = name;
        this.price = ProductPrice.from(price);
    }

    public static Product of(String name, long price) {
        return new Product(name, price);
    }

    public static Product of(Long id, String name, long price) {
        return new Product(id, name, price);
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

    public long getPrice() {
        return price.value;
    }

}
