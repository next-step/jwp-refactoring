package kitchenpos.product.domain;

import java.util.Objects;
import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.PriceEmptyException;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Embedded
    @AttributeOverride(name = "amount", column = @Column(name = "price", nullable = false))
    private Price price = Price.wonOf(0);

    public Product() {
    }

    public Product(Long id) {
        this.id = id;
    }

    public Product(String name, Price price) {
        this(null, name, price);
    }

    public Product(Long id, String name, Price price) {
        validationPrice(price);
        this.id = id;
        this.name = name;
        this.price = price;
    }

    private void validationPrice(Price price) {
        if (Objects.isNull(price)) {
            throw new PriceEmptyException();
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

    public Price multiplyPrice(Quantity quantity) {
        return price.multiply(quantity);
    }
}
