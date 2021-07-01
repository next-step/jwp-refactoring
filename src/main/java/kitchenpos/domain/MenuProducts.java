package kitchenpos.domain;

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

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

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

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public List<MenuProduct> toCollection() {
        return Collections.unmodifiableList(menuProducts);
    }

    public Price sumAmount() {
        Price amount = menuProducts.stream()
                .map(item -> item.getAmount())
                .reduce(new Price(0), (b, a) -> new Price(b.getPrice().add(a.getPrice())));

        return amount;
    }

    public void addAll(List<MenuProduct> menuProducts) {
        this.menuProducts.addAll(menuProducts);
    }

    public void addAll(List<MenuProduct> menuProducts, Menu menu) {
        menuProducts.forEach(item -> item.changeMenu(menu));
        this.menuProducts.addAll(menuProducts);
    }
}
