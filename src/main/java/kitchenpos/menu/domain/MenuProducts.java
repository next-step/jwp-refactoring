package kitchenpos.menu.domain;

import kitchenpos.menu.application.MenuProductNotFoundException;
import org.springframework.util.CollectionUtils;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {
    private static final String NOT_EXIST_MENU_PRODUCT = "존재하지않는 메뉴상품";
    private static final String INVALID_PRICE = "올바르지 않은 금액입니다.";

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
        menuProducts = new ArrayList<>();
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void add(MenuProduct menuProduct) {
        menuProducts.add(menuProduct);
    }

    public boolean isEmpty() {
        return CollectionUtils.isEmpty(menuProducts);
    }

    public void validateIsEmpty() {
        if (menuProducts.isEmpty()) {
            throw new MenuProductNotFoundException(NOT_EXIST_MENU_PRODUCT);
        }
    }

    public void registerMenu(Menu menu) {
        for (MenuProduct menuProduct : menuProducts) {
            menuProduct.registerMenu(menu);
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
