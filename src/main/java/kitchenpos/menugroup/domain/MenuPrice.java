package kitchenpos.menugroup.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.InvalidPriceException;
import kitchenpos.menu.domain.MenuProduct;

import javax.persistence.Embeddable;
import java.math.BigDecimal;
import java.util.List;

@Embeddable
public class MenuPrice extends Price {
    private BigDecimal price;

    protected MenuPrice() {
    }

    public MenuPrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public void compareSumOfMenuProducts(final List<MenuProduct> menuProducts) {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.calculatePrice());
        }

        if (price.compareTo(sum) > 0) {
            throw new InvalidPriceException("메뉴의 가격은 메뉴 상품들의 총 합보다 작아야 합니다.");
        }
    }

    public BigDecimal getValue() {
        return price;
    }

    public void setValue(BigDecimal price) {
        this.price = price;
    }
}
