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
        validateNegativePrice(price);
        this.price = price;
    }

    public MenuPrice(Long price) {
        this(new BigDecimal(price));
    }

    public BigDecimal getPrice() {
        return price;
    }

    private void validateNegativePrice(BigDecimal price) {
        final int negativeCriteria = 0;
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < negativeCriteria) {
            throw new IllegalArgumentException();
        }
    }

    public boolean notMatch(MenuPrice otherMenuPrice) {
        final int correctCriteria = 0;
        return price.compareTo(otherMenuPrice.getPrice()) != correctCriteria;
    }
}
