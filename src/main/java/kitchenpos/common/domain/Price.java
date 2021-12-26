package kitchenpos.common.domain;

import kitchenpos.common.exception.NegativePriceException;
import kitchenpos.menu.domain.MenuProduct;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Embeddable
public class Price {
    private BigDecimal price;

    public Price() {
    }

    public Price(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    private void validatePrice(BigDecimal price) {
        if (Objects.isNull(price) || price.compareTo(BigDecimal.ZERO) < 0) {
            throw new NegativePriceException();
        }
    }

    public BigDecimal getPrice() {
        return price;
    }

    public boolean isPossibleMenu(List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
                .map(menuProduct -> menuProduct.multiply(price))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return price.compareTo(sum) < 0;
    }
}
