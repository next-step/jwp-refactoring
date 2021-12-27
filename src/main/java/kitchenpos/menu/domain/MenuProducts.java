package kitchenpos.menu.domain;

import org.springframework.util.Assert;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {

    }

    private MenuProducts(List<MenuProduct> menuProducts) {
        Assert.notEmpty(menuProducts, "메뉴 상품은 필수입니다.");
        this.menuProducts = menuProducts;
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public List<MenuProduct> getMenuProducts() {
        return Collections.unmodifiableList(menuProducts);
    }

    public BigDecimal calculatorTotalPrice() {
        return menuProducts.stream()
                .map(MenuProduct::calculateTotalPrice)
                .reduce(BigDecimal::add)
                .orElse(BigDecimal.ZERO);
    }

    public void changeMenu(Menu menu) {
        Assert.notNull(menu, "메뉴는 필수입니다.");
        menuProducts.forEach(menuProduct ->
                menuProduct.changeMenu(menu));
    }
}
