package kitchenpos.menu.domain;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts;

    public MenuProducts() {
        menuProducts = new ArrayList<>();
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }


    public void registerProduct(Menu menu) {
        this.menuProducts.forEach(
                menuProduct -> menuProduct.registerMenu(menu)
        );
    }

    public BigDecimal calculationTotalAmount() {
        BigDecimal productsPrice = BigDecimal.ZERO;
        for (MenuProduct menuProduct: menuProducts) {
            productsPrice = productsPrice.add(menuProduct.getPriceByQuantity());
        }
        return productsPrice;
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
