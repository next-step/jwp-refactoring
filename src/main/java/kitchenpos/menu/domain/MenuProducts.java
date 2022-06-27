package kitchenpos.menu.domain;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {
    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        validateMenuProducts(menuProducts);
        this.menuProducts = menuProducts;
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    private static void validateMenuProducts(List<MenuProduct> menuProducts) {
        if (Objects.isNull(menuProducts)) {
            throw new IllegalArgumentException("메뉴상품들이 필요 합니다.");
        }
    }

    public List<MenuProduct> readOnlyMenuProducts() {
        return Collections.unmodifiableList(this.menuProducts);
    }

    public void addMenu(Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.addMenu(menu));
    }
}
