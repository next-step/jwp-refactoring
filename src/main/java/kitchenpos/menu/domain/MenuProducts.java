package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public MenuProducts() { }

    public MenuProducts(List<MenuProduct> menuProducts, Long menuId) {
        this.menuProducts = menuProducts;
//        allMenuProductsArePartOf(menuId);
    }

    public List<MenuProduct> get() {
        return menuProducts;
    }

//    private void allMenuProductsArePartOf(Long id) {
//        this.menuProducts.stream()
//                .forEach(menuProduct -> menuProduct.setProductId(id));
//    }
}
