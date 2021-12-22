package kitchenpos.menu.domain;

import static kitchenpos.common.exception.ExceptionMessage.*;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.common.domain.Price;
import kitchenpos.common.exception.BadRequestException;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> getValue() {
        return menuProducts;
    }

    public void validateMenuPrice(Price menuPrice) {
        BigDecimal sumPrice = sumProductsPrice();
        if (menuPrice.isGreaterThanSumPrice(sumPrice)) {
            throw new BadRequestException(WRONG_VALUE);
        }
    }

    private BigDecimal sumProductsPrice() {
        return menuProducts.stream().map(
                MenuProduct::multiplyQuantityToPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MenuProducts that = (MenuProducts)o;
        return Objects.equals(menuProducts, that.menuProducts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuProducts);
    }
}
