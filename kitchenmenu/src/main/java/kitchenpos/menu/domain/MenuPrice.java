package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embeddable;
import kitchenpos.menuconstants.MenuErrorMessages;

@Embeddable
public class MenuPrice {

    @Column(name = "price", nullable = false)
    private BigDecimal price;

    public MenuPrice() {
        this.price = BigDecimal.ZERO;
    }

    public MenuPrice(BigDecimal price) {
        validatePrice(price);
        this.price = price;
    }

    public MenuPrice(int price) {
        this(new BigDecimal(price));
    }

    private void validatePrice(BigDecimal val) {
        if (Objects.isNull(val)) {
            throw new IllegalArgumentException(MenuErrorMessages.MENU_PRICE_IS_NULL);
        }
        if (val.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException(MenuErrorMessages.MENU_PRICE_CANNOT_BE_LESS_THAN_ZERO);
        }
    }

    public void add(BigDecimal val) {
        BigDecimal result = this.price.add(val);
        validatePrice(result);
        this.price = result;
    }

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
