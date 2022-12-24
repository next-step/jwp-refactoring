package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;

import java.util.List;

public class MenuProducts {
    private static final String INVALID_PRICE = "메뉴 가격은 구성 메뉴 단품 가격의 합보다 클 수 없습니다.";

    private List<MenuProduct> menuProducts;

    public MenuProducts(List<MenuProduct> menuProducts) {
        this.menuProducts = menuProducts;
    }

    public void validateMenuPrice(Price menuPrice) {
        Price menuProductPriceSum = getMenuProductPriceSum();

        if (menuPrice.isGreaterThan(menuProductPriceSum)) {
            throw new IllegalArgumentException(INVALID_PRICE);
        }
    }

    public Price getMenuProductPriceSum() {
        int productsPriceSum = menuProducts.stream()
                .mapToInt(menuProduct -> menuProduct.getMenuProductPrice().intValue())
                .sum();
        return new Price(productsPriceSum);
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }

}