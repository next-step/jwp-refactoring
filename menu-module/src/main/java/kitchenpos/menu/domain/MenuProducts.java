package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menuId", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<Long> toMenuProductIds() {
        return this.menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public List<MenuProduct> getMenuProducts() {
        return new ArrayList<>(menuProducts);
    }
}
