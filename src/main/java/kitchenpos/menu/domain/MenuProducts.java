package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.exception.PriceValueNotAcceptableException;

@Embeddable
public class MenuProducts {

    private static final String ERROR_MESSAGE_MENU_PRICE_HIGH = "메뉴 가격은 상품 리스트의 가격 합보다 작거나 같아야 합니다.";

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected void changeMenuProducts(List<MenuProduct> inputMenuProducts, BigDecimal menuPrice) {
        menuProducts.clear();
        if (isEmptyList(inputMenuProducts)) {
            validatePriceIsZero(menuPrice);
            return;
        }
        validateMenuPriceIsLessThanMenuProductsSum(menuPrice, inputMenuProducts);
        menuProducts.addAll(inputMenuProducts);
    }

    private boolean isEmptyList(List<MenuProduct> inputMenuProducts) {
        return Objects.isNull(inputMenuProducts) || inputMenuProducts.size() == 0;
    }

    private void validateMenuPriceIsLessThanMenuProductsSum(BigDecimal menuPrice,
        List<MenuProduct> menuProducts) {
        BigDecimal sum = menuProducts.stream()
            .map(MenuProduct::getMenuProductPrice)
            .reduce(BigDecimal.ZERO, (subSum, menuProductPrice) -> subSum.add(menuProductPrice));

        if (menuPrice.compareTo(sum) > 0) {
            throw new PriceValueNotAcceptableException(ERROR_MESSAGE_MENU_PRICE_HIGH);
        }
    }

    private void validatePriceIsZero(BigDecimal menuPrice) {
        if (menuPrice.compareTo(BigDecimal.ZERO) > 0) {
            throw new PriceValueNotAcceptableException("메뉴상품이 없는 경우 메뉴 가격은 0 이어야 합니다.");
        }
    }

    public List<MenuProduct> getMenuProducts() {
        return menuProducts;
    }
}
