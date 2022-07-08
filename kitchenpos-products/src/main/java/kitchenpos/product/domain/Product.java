package kitchenpos.product.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Price;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    private Price price;

    protected Product() {
    }

    public Product(Product product) {
        this(product.id, product.name, product.price);
    }

    private Product(String name, Price price) {
        this(null, name, price);
    }

    public Product(Long id, String name, Price price) {
        this.id = id;
        this.name = name;
        this.price = price;
        validateNonNullFields();
    }

    public static Product of(String name, long price) {
        return new Product(name, Price.from(price));
    }

    public static Product of(String name, BigDecimal price) {
        return new Product(name, Price.from(price));
    }

    public static Product of(Long id, String name, BigDecimal price) {
        return new Product(id, name, Price.from(price));
    }

    private void validateNonNullFields() {
        if (name == null || price == null) {
            throw new IllegalArgumentException("이름, 가격은 상품의 필수 사항입니다.");
        }
    }

    public Price calculateTotal(long quantity) {
        return price.multiply(quantity);
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getPrice() {
        return price.value();
    }
}
