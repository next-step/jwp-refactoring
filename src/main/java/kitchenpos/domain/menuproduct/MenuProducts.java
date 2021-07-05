package kitchenpos.domain.menuproduct;

import kitchenpos.domain.Price;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.product.Products;

import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    public static List<MenuProduct> create(List<MenuProductCreate> menuProducts, Menu menu, Products products) {
        return menuProducts.stream()
                .map(item -> new MenuProduct(menu, products.findById(item.getProductId()), item.getQuantity()))
                .collect(Collectors.toList());
    }

    public static List<MenuProduct> create(List<MenuProductCreate> menuProducts, Products products) {
        return menuProducts.stream()
                .map(item -> new MenuProduct(null, products.findById(item.getProductId()), item.getQuantity()))
                .collect(Collectors.toList());
    }

    public MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts, Menu menu) {
        this.menuProducts = new ArrayList<>(menuProducts);
        this.menuProducts.forEach(item -> item.changeMenu(menu));
    }

    public Price sumAmount() {
        Price amount = menuProducts.stream()
                .map(item -> item.getAmount())
                .reduce(new Price(0), (before, appender) -> before.plus(appender));

        return amount;
    }

    public List<MenuProduct> toCollection() {
        return Collections.unmodifiableList(menuProducts);
    }
}
