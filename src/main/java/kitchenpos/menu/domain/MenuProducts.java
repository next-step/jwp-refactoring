package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.domain.Price;
import org.springframework.util.CollectionUtils;

@Embeddable
public class MenuProducts {
    public static final String ERROR_MESSAGE_MENU_PRODUCTS_IS_EMPTY = "메뉴 상품 목록은 필수입니다.";
    @OneToMany(mappedBy = "menu", cascade = CascadeType.PERSIST)
    private List<MenuProduct> products = new ArrayList<>();

    protected MenuProducts() {}

    private MenuProducts(List<MenuProduct> products) {
        validateIsEmpty(products);
        this.products = new ArrayList<>(products);
    }

    public static MenuProducts from(List<MenuProduct> products) {
        return new MenuProducts(products);
    }

    private void validateIsEmpty(List<MenuProduct> products) {
        if (CollectionUtils.isEmpty(products)) {
            throw new InvalidParameterException(ERROR_MESSAGE_MENU_PRODUCTS_IS_EMPTY);
        }
    }

    Price totalPrice() {
        Price totalPrice = Price.from(BigDecimal.ZERO);
        for (MenuProduct menuProduct : products) {
            totalPrice = totalPrice.sum(menuProduct.price());
        }
        return totalPrice;
    }

    void changeMenu(Menu menu) {
        products.forEach(menuProduct -> menuProduct.changeMenu(menu));
    }

    public List<MenuProduct> list() {
        return Collections.unmodifiableList(products);
    }
}
