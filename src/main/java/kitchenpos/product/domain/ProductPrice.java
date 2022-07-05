package kitchenpos.product.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode
@Embeddable
public class ProductPrice {

    @Column
    private BigDecimal price = BigDecimal.ZERO;

    public ProductPrice() {
    }

    public ProductPrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    private void validatePrice(BigDecimal value){
        if (Objects.isNull(value) || value.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public ProductPrice multiply(BigDecimal value){
        price = price.multiply(value);
        return this;
    }

    public void add(ProductPrice value){
        price = price.add(value.getPrice());
    }

    public int compareTo(ProductPrice value){
        return price.compareTo(value.getPrice());
    }

}

