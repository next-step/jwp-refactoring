package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.constants.ErrorMessages;

@Embeddable
public class MenuPrice implements Comparable<MenuPrice> {

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public MenuPrice() {
        this.price = BigDecimal.ZERO;
    }

    public MenuPrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public MenuPrice(int i) {
        this(new BigDecimal(i));
    }

    private void validatePrice(BigDecimal val) {
        if (Objects.isNull(val)) {
            throw new IllegalArgumentException(ErrorMessages.MENU_PRICE_IS_NULL);
        }
        if (val.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(ErrorMessages.MENU_PRICE_CANNOT_BE_LESS_THAN_ZERO);
        }
    }

    public void add(BigDecimal val) {
        BigDecimal result = this.price.add(val);
        validatePrice(result);
        this.price = result;
    }

    public int compareTo(BigDecimal val) {
        return this.price.compareTo(val);
    }

    @Override
    public int compareTo(MenuPrice o) {
        return this.price.compareTo(o.price);
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuPrice menuPrice1 = (MenuPrice) o;
        return Objects.equals(price, menuPrice1.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(price);
    }
}
