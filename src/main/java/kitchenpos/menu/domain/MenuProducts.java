package kitchenpos.menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = new ArrayList<>(menuProducts);
    }

    public List<MenuProduct> getList() {
        return this.menuProducts;
    }

    public BigDecimal getSumPrice() {
        BigDecimal sum = BigDecimal.ZERO;
        for (MenuProduct menuProduct : menuProducts) {
            sum = sum.add(menuProduct.getProduct()
                    .getPrice().multiply(BigDecimal.valueOf(menuProduct.getQuantity())));
        }

        return sum;
    }
}
