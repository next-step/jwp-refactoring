package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;

import kitchenpos.common.domain.Price;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected MenuProducts() {
    }

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public List<MenuProduct> findAll() {
        return Collections.unmodifiableList(this.menuProducts);
    }

    public void updateMenu(Menu menu) {
        checkPriceExpensiveThanProductsPriceSum(new Price(menu.getPrice()));
        menuProducts.forEach(menuProduct -> menuProduct.updateMenu(menu));
    }

    private void checkPriceExpensiveThanProductsPriceSum(Price menuPrice) {
        if (menuPrice.isExpensiveThan(findPriceSum())) {
            throw new IllegalArgumentException("메뉴의 가격이 메뉴의 속하는 상품 가격의 합보다 비쌉니다.");
        }
    }

    protected Price findPriceSum() {
        Price priceSum = new Price();
        for (final MenuProduct menuProduct : menuProducts) {
            priceSum.add(menuProduct.findPriceForQuantity());
        }
        return priceSum;
    }
}
