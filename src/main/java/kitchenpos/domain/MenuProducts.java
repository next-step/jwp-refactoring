package kitchenpos.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Embeddable;
import javax.persistence.OneToMany;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

@Embeddable
public class MenuProducts {
    private static final int MIE_MENU_PRODUCT_QUANTITY = 1;
    private static final int SEQ_START_INDEX = 1;

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "menu")
    private List<MenuProduct> menuProductElements = new ArrayList<>();

    protected MenuProducts() {

    }

    private MenuProducts(List<MenuProduct> menuProductElements) {
        validMenuProducts(menuProductElements);
        this.menuProductElements = menuProductElements;
    }

    private MenuProducts(MenuProduct menuProduct) {
        validateProduct(menuProduct);
        menuProductElements.add(menuProduct);
    }

    public static MenuProducts from(List<MenuProduct> menuProducts) {
        return new MenuProducts(menuProducts);
    }

    public static MenuProducts from(MenuProduct menuProduct) {
        return new MenuProducts(menuProduct);
    }

    public Amount totalAmount() {
        List<Amount> amounts = menuProductElements.stream()
                .map(MenuProduct::getAmount).collect(Collectors.toList());

        return Amount.createSumAmounts(amounts);
    }

    public List<MenuProduct> value() {
        return Collections.unmodifiableList(menuProductElements);
    }


    private void validMenuProducts(List<MenuProduct> menuProductElements) {
        if (CollectionUtils.isEmpty(menuProductElements) || menuProductElements.size() < MIE_MENU_PRODUCT_QUANTITY) {
            throw new IllegalArgumentException("메뉴상품은 1개 이상이 존재 해야합니다.");
        }
        if (menuProductElements.stream().anyMatch(Objects::isNull)) {
            throw new IllegalArgumentException("메뉴상품이 존재하지 않는 항목이 있습니다.");
        }
    }

    private void validateProduct(MenuProduct menuProduct) {
        if (ObjectUtils.isEmpty(menuProduct)) {
            throw new IllegalArgumentException("메뉴상품이 비어있습니다");
        }
    }

    public void changeMenu(Menu menu) {
        this.menuProductElements.forEach((menuProduct)-> menuProduct.changeMenu(menu));
    }
}
