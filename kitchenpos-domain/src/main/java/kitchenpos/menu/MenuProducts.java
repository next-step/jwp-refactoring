package kitchenpos.menu;

import kitchenpos.product.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Embeddable
public class MenuProducts {
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public BigDecimal calculateSumOfMenuProducts(final List<Product> products) {
        BigDecimal sum = BigDecimal.ZERO;

        for (MenuProduct menuProduct : menuProducts) {
            Product product = menuProduct.findProduct(products);
            sum = sum.add(menuProduct.calculatePrice(product));
        }

        return sum;
    }

    public void addMenuIdToMenuProducts(final Long menuId) {
        this.menuProducts = menuProducts.stream()
                .map(menuProduct -> menuProduct.addMenuId(menuId))
                .collect(Collectors.toList());
    }

    public List<Long> getProductIds() {
        return menuProducts.stream()
                .map(MenuProduct::getProductId)
                .collect(Collectors.toList());
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
