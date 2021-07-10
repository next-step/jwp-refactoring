package kitchenpos.menu;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "menu_id")
    private final List<MenuProduct> menuProductList;

    protected MenuProducts() {
        this.menuProductList = new ArrayList<>();
    }

    private MenuProducts(List<MenuProduct> menuProductList) {
        this.menuProductList = menuProductList;
    }

    public static MenuProducts of(List<MenuProduct> menuProductList) {
        return new MenuProducts(menuProductList);
    }

    public Boolean contain(MenuProduct menuProduct) {
        return menuProductList.contains(menuProduct);
    }

    public List<MenuProduct> toCollection() {
        return Collections.unmodifiableList(menuProductList);
    }

    public Boolean isSumUnder(BigDecimal price) {
        BigDecimal sum = menuProductList.stream()
            .map(menuProduct -> menuProduct.totalPrice())
            .reduce(BigDecimal.ZERO, (a, b) -> a.add(b));

        return price.compareTo(sum) > 0;
    }

    public void assginMenu(Long menuId) {
        menuProductList.forEach(menuProduct -> menuProduct.assginMenu(menuId));
    }

}
