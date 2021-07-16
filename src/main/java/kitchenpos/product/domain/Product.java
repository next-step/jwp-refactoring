package kitchenpos.product.domain;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.generic.price.domain.Price;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(String name, Price price) {
        this(null, name, price);
    }

    Product(Long id, String name, Price price) {
        checkArguments(name, price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void checkArguments(String name, Price price) {
        if (Objects.isNull(name) || Objects.isNull(price)) {
            throw new IllegalArgumentException("제품을 생성하려면 모든 필수값이 입력되어야 합니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Price getPrice() {
        return price;
    }

    public boolean isSatisfiedBy(ProductOption productOption) {
        if (!this.name.equals(productOption.getName())) {
            return false;
        }

        return this.price.hasSameValueAs(productOption.getPrice());
    }
}
