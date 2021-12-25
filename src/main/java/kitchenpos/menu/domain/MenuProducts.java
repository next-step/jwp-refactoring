package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.exception.PriceNotAcceptableException;
import kitchenpos.common.vo.Price;

@Embeddable
public class MenuProducts {

    private static final String ERROR_MESSAGE_MENU_PRICE_HIGH = "메뉴 가격은 상품 리스트의 가격 합보다 작거나 같아야 합니다.";

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected void changeMenuProducts(List<MenuProduct> inputMenuProducts, Price price) {
        menuProducts.clear();
        if (isEmptyList(inputMenuProducts)) {
            validatePriceIsZero(price);
            return;
        }
        validateMenuPriceIsLessThanMenuProductsSum(price, inputMenuProducts);
        menuProducts.addAll(inputMenuProducts);
    }

    private boolean isEmptyList(List<MenuProduct> inputMenuProducts) {
        return Objects.isNull(inputMenuProducts) || inputMenuProducts.size() == 0;
    }

    private void validateMenuPriceIsLessThanMenuProductsSum(Price price,
        List<MenuProduct> menuProducts) {
        List<Price> menuProductPrices = getMenuProductPrices(menuProducts);
        Price sumOfMenuProducts = Price.sumPrices(menuProductPrices);

        if (price.isBiggerThan(sumOfMenuProducts)) {
            throw new PriceNotAcceptableException(ERROR_MESSAGE_MENU_PRICE_HIGH);
        }
    }

    private List<Price> getMenuProductPrices(List<MenuProduct> menuProducts) {
        return menuProducts.stream()
            .map(MenuProduct::getMenuPrice)
            .collect(Collectors.toList());
    }

    private void validatePriceIsZero(Price price) {
        if (price.isBiggerThan(Price.valueOf(BigDecimal.ZERO))) {
            throw new PriceNotAcceptableException("메뉴상품이 없는 경우 메뉴 가격은 0 이어야 합니다.");
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
