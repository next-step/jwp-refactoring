package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.product.domain.ProductPrice;

@Embeddable
public class MenuPrice {

    @Column
    private BigDecimal price = BigDecimal.ZERO;

    public MenuPrice() {
    }

    public MenuPrice(BigDecimal price) {
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

    public MenuPrice multiply(BigDecimal value){
        price = price.multiply(value);
        return this;
    }

    public int compareTo(ProductPrice value){
        return price.compareTo(value.getPrice());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof MenuPrice)) {
            return false;
        }
        MenuPrice menuPrice = (MenuPrice) o;
        return Objects.equals(getPrice(), menuPrice.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPrice());
    }
}

