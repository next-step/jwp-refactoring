package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.constant.ErrorCode;
import kitchenpos.common.domain.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    private MenuProducts(List<MenuProduct> menuProducts) {
        validateMenuProductsNotEmpty(menuProducts);
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    private void validateMenuProductsNotEmpty(List<MenuProduct> menuProducts) {
        if (Objects.isNull(menuProducts) || menuProducts.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.MENU_PRODUCT_NOT_EMPTY.getErrorMessage());
        }
    }

    public Price totalPrice() {
        Price totalPrice = Price.from(BigDecimal.ZERO);
        for(MenuProduct menuProduct: menuProducts) {
            totalPrice = totalPrice.add(menuProduct.totalPrice());
        }
        return totalPrice;
    }

    public void setMenu(final Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.assignMenu(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
