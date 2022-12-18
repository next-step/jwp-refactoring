package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.constant.ErrorCode;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST, orphanRemoval = true)
    private List<MenuProduct> values = new ArrayList<>();

    protected MenuProducts() {}

    private MenuProducts(List<MenuProduct> menuProducts) {
        validateMenuProductsNotEmpty(menuProducts);
        this.values = new ArrayList<>(menuProducts);
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    private void validateMenuProductsNotEmpty(List<MenuProduct> menuProducts) {
        if (Objects.isNull(menuProducts) || menuProducts.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.MENU_PRODUCT_NOT_EMPTY.getErrorMessage());
        }
    }

    public void setMenu(final Menu menu) {
        values.forEach(menuProduct -> menuProduct.assignMenu(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(values);
    }
}
