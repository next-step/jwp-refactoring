package kitchenpos.domain;

import static kitchenpos.exception.ErrorCode.PRICE_NOT_EXISTS_OR_LESS_THAN_ZERO;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.exception.KitchenposException;

@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private BigDecimal price;

    protected Product() {
    }

    public Product(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public Product(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
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

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(final BigDecimal price) {
        this.price = price;
    }

    public void validatePrice() {
        if (isPriceNull(this.price) || isLessThanZero()) {
            throw new KitchenposException(PRICE_NOT_EXISTS_OR_LESS_THAN_ZERO);
        }
    }

    private boolean isPriceNull(BigDecimal price){
        return Objects.isNull(price);
    }

    private boolean isLessThanZero(){
        return this.price.compareTo(BigDecimal.ZERO) < 0;
    }
}
