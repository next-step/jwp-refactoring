package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {

    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

    public void add(Product product, long quantity) {
        this.add(new MenuProduct(product, quantity));
    }

    public void add(MenuProduct menuProduct) {
        this.menuProducts.add(menuProduct);
    }

    public MenuPrice totalPrice() {
        return new MenuPrice(
                this.menuProducts.stream()
                .mapToLong(p -> p.getProduct().multiplyQuantity(p.getQuantity()))
                .sum()
        );
    }
}
