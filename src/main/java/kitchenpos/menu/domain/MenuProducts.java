package kitchenpos.menu.domain;

import kitchenpos.global.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.OneToMany;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts, Price price, Menu menu) {
        validMenu(menuProducts);
        validPrice(menuProducts, price);
        changeMenu(menuProducts, menu);
        this.menuProducts = menuProducts;
    }

    private void validMenu(List<MenuProduct> menuProducts) {
        menuProducts.forEach(MenuProduct::validProduct);
    }

    private void validPrice(List<MenuProduct> menuProducts, Price price) {
        Price totalPrice = getTotalPrice(menuProducts);
        validComparePrice(totalPrice, price);
    }

    private Price getTotalPrice(List<MenuProduct> menuProducts) {
        Price total = new Price(BigDecimal.ZERO);
        for (MenuProduct menuProduct : menuProducts) {
            total.add(menuProduct.getTotalPrice());
        }
        return total;
    }

    private void validComparePrice(Price totalPrice, Price price) {
        if (totalPrice.getValue().compareTo(price.getValue()) > 0) {
            throw new IllegalArgumentException("메뉴 상품보다 메뉴의 가격이 높을 수 없습니다.");
        }
    }

    private void changeMenu(List<MenuProduct> menuProducts, Menu menu) {
        menuProducts.forEach(menuProduct -> menuProduct.changeMenu(menu));
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
