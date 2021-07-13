package kitchenpos.product.domain;

import static java.util.Objects.*;

import java.math.BigDecimal;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import kitchenpos.domain.vo.Name;
import kitchenpos.domain.vo.Price;
import kitchenpos.domain.vo.Quantity;

@Entity
@Table
public class Product {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @AttributeOverride(name = "value", column = @Column(name = "name", nullable = false))
    private Name name;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price", nullable = false))
    private Price price;

    protected Product() {}

    public Product(Long id, Name name, Price price) {
        validateNonNull(name, price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public Product(String name, BigDecimal price) {
        this(Name.valueOf(name), Price.wonOf(price));
    }

    public Product(Name name, Price price) {
        this(null, name, price);
    }

    private void validateNonNull(Name name, Price price) {
        if (isNull(name) || isNull(price)) {
            throw new IllegalArgumentException("상품의 이름과 가격은 필수 정보입니다.");
        }
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

    public BigDecimal getPriceAmount() {
        return price.getAmount();
    }

    public Price calculatePrice(Quantity quantity) {
        return this.price.times(quantity);
    }
}
