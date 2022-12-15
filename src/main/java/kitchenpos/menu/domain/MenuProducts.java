package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.constant.ErrorCode;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {}

    private MenuProducts(List<MenuProduct> menuProducts) {
        validateMenuProductsIsEmpty(menuProducts);
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    private void validateMenuProductsIsEmpty(List<MenuProduct> menuProducts) {
        if(menuProducts.isEmpty()) {
            throw new IllegalArgumentException(ErrorCode.메뉴_상품은_비어있을_수_없음.getErrorMessage());
        }
    }

    public void setUpMenu(final Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.setUpMenu(menu));
    }

    public List<MenuProduct> findMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }
}
