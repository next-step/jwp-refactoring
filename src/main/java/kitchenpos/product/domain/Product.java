package kitchenpos.product.domain;

import kitchenpos.common.Money;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class Product {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "price")
    private Money price;

    protected Product() {
    }

    public Product(final String name, final Money price) {
        validate(name, price);
        this.name = name;
        this.price = price;
    }

    private void validate(final String name, final Money price) {
        if (Objects.isNull(name) || Objects.isNull(price)) {
            throw new IllegalArgumentException("이름과 가격은 필수 정보이다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Money getPrice() {
        return price;
    }
}
