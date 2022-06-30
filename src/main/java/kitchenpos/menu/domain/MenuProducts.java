package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> list;

    public MenuProducts() {
        list = new ArrayList<>();
    }

    public MenuProducts(Menu menu, BigDecimal menuPrice, List<MenuProduct> list) {
        validateMenuProducts(menu, menuPrice, list);
        this.list = list;
    }

    private void validateMenuProducts(Menu menu, BigDecimal menuPrice, List<MenuProduct> list) {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : list) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().getValue().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
            menuProduct.setMenu(menu);
        }

        if (menuPrice.compareTo(sum) > 0) {
            throw new IllegalArgumentException();
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return list;
    }
}
