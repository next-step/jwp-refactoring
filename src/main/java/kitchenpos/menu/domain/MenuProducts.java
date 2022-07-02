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

    public MenuProducts(List<MenuProduct> list) {
        this.list = list;
    }

    public MenuProducts(Menu menu, List<MenuProduct> list) {
        this.list = list;
        for (final MenuProduct menuProduct : list) {
            menuProduct.connectedBy(menu);
        }
    }

    public BigDecimal calculateTotalPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (final MenuProduct menuProduct : list) {
            final Product product = menuProduct.getProduct();
            sum = sum.add(product.getPrice().getValue().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }
        return sum;
    }

    public List<MenuProduct> getMenuProducts() {
        return list;
    }
}
