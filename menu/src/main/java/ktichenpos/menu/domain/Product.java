package ktichenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.util.Assert;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Embedded
    private Price price;

    public Product() {
    }

    private Product(String name, Price price) {
        Assert.notNull(name, "이름은 필수입니다.");
        Assert.notNull(price, "가격은 필수입니다.");
        this.name = name;
        this.price = price;
    }

    public static Product of(String name, Price price) {
        return new Product(name, price);
    }

    public Long id() {
        return id;
    }
    public String name() {
        return name;
    }

    public Price price() {
        return price;
    }

    @Override
    public String toString() {
        return "Product{" +
            "id=" + id +
            ", name=" + name +
            ", price=" + price +
            '}';
    }
}
