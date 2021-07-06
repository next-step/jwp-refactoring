package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
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

    public void assginMenu(Menu menu) {
        menuProductList.forEach(menuProduct -> menuProduct.assginMenu(menu));
    }

}
