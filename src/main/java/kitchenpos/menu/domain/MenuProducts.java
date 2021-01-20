package kitchenpos.menu.domain;

import kitchenpos.common.Price;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

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
        return Collections.unmodifiableList(menuProducts);
    }

    public void updateMenu(Menu menu) {
        checkMenuPrice(new Price(menu.getPrice()));
        menuProducts.forEach(menuProduct -> menuProduct.updateMenu(menu));
    }

    private void checkMenuPrice(Price menuPrice) {
        if (menuPrice.isExpensive(findPriceSum())) {
            throw new IllegalArgumentException("메뉴 가격이 속한 상품 가격들의 합보다 비쌉니다.");
        }
    }

    protected Price findPriceSum() {
        Price priceSum = new Price();
        for (MenuProduct menuProduct : menuProducts) {
            priceSum.add(menuProduct.getPricePerQuantity());
        }
        return priceSum;
    }
}
