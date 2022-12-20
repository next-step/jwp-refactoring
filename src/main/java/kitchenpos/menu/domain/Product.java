package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

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

    @Embedded
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

    public Long getId() {
        return id;
    }
    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.getValue();
    }
    @Override
    public int hashCode() {
        return Objects.hash(id, name, price);
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
        return Objects.equals(id, product.id) && Objects.equals(name, product.name)
            && Objects.equals(price, product.price);
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
