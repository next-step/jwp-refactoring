package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.exception.PriceNotAcceptableException;
import kitchenpos.common.vo.Price;

@Embeddable
public class MenuProducts {

    @OneToMany(mappedBy = "menu", cascade = CascadeType.ALL)
    private List<MenuProduct> menuProducts = new ArrayList<>();

    protected void changeMenuProducts(List<MenuProduct> inputMenuProducts, Price price) {
        menuProducts.clear();
        if (isEmptyList(inputMenuProducts)) {
            validatePriceIsZero(price);
            return;
        }
        menuProducts.addAll(inputMenuProducts);
    }

    private boolean isEmptyList(List<MenuProduct> inputMenuProducts) {
        return Objects.isNull(inputMenuProducts) || inputMenuProducts.size() == 0;
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
