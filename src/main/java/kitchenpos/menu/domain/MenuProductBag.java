package kitchenpos.menu.domain;

import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
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

    public List<Long> productIds() {
        return this.menuProductList.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public void setMenuToMenuProducts(Menu menu) {
        this.menuProductList.forEach(it -> it.updateMenu(menu));
    }
}
