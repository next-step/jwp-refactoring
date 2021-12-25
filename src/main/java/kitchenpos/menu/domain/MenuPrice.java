package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.Objects;

@Embeddable
public class MenuPrice {

    @Column(name = "price", precision = 19, scale = 2)
    private BigDecimal price;

    protected MenuPrice() {

    }

    public MenuPrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public BigDecimal getPrice() {
        return price;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException();
        }
    }

    public boolean notMatch(MenuPrice otherMenuPrice) {
        return price.compareTo(otherMenuPrice.getPrice()) != 0;
    }
}
