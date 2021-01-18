package kitchenpos.menu.domain;

import kitchenpos.generic.Money;

import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import java.util.ArrayList;
import java.util.List;

@Embeddable
public class MenuProducts {
    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL, orphanRemoval = true)
    private final List<MenuProduct> products = new ArrayList<>();

    protected MenuProducts() {
    }

    public List<MenuProduct> getProducts() {
        return products;
    }

    public void addAll(Money price, List<MenuProduct> menuProducts) {
        checkGraterThanMenuPrice(price, menuProducts.stream()
                .map(MenuProduct::getAmount)
                .reduce(Money.ZERO_MONEY, Money::sum));
        this.products.addAll(menuProducts);
    }

    private void checkGraterThanMenuPrice(Money source, Money target) {
        if (source.isGraterThan(target)) {
            throw new IllegalArgumentException("메뉴의 가격이 상품 가격 보다 큽니다.");
        }
    }

}
