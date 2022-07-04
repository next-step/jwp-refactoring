package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    // TODO 메뉴에 동일한 메뉴 상품은 포함될 수 없으므로 List를 Set으로 표현, MenuProduct의 equasl/hascode를 product
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    protected MenuProducts() {

    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public static MenuProducts of(Menu menu, List<MenuProduct> menuProducts) {
        menuProducts.forEach(it -> it.setMenu(menu));
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public BigDecimal totalMenuProductPrice() {
        return menuProducts.stream()
            .map(MenuProduct::multiplyQuantityToPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
