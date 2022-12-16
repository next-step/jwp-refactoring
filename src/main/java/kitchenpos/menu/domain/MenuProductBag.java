package kitchenpos.menu.domain;

import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class MenuProductBag {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY)
    private List<MenuProduct> menuProductList = new ArrayList<>();

    private MenuProductBag(List<MenuProduct> menuProductList) {
        this.menuProductList = menuProductList;
    }

    public static MenuProductBag from(List<MenuProduct> menuProductList) {
        return new MenuProductBag(menuProductList);
    }

    protected MenuProductBag() {
    }

    public List<MenuProduct> getMenuProductList() {
        return menuProductList;
    }

    public List<Product> productList() {
        return this.menuProductList.stream().map(MenuProduct::getProduct)
                .collect(Collectors.toList());
    }

    public Price totalPrice() {
        return Price.from(BigDecimal.valueOf(this.menuProductList.stream()
                .map(MenuProduct::totalProductPrice)
                .mapToInt(BigDecimal::intValue)
                .sum()));
    }

    public void setMenuToMenuProducts(Menu menu) {
        this.menuProductList.forEach(it -> it.updateMenu(menu));
    }
}
