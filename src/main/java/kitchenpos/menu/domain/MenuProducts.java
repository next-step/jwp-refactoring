package kitchenpos.menu.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import kitchenpos.common.exception.InvalidParameterException;
import org.springframework.util.CollectionUtils;

@Embeddable
public class MenuProducts {
    private static final String ERROR_MESSAGE_MENU_PRODUCTS_IS_EMPTY = "메뉴 상품 목록은 필수입니다.";

    @OneToMany(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id", nullable = false)
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

    public List<MenuProduct> list() {
        return Collections.unmodifiableList(products);
    }
}
