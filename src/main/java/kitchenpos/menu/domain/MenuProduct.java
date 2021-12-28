package kitchenpos.menu.domain;

import java.util.*;

import javax.persistence.*;

import kitchenpos.common.*;

@Entity
public class MenuProduct {
    private static final String MENU = "메뉴";
    private static final String PRODUCT = "상품";
    private static final String QUANTITY_IS_NEGATIVE_EXCEPTION_STATEMENT = "수량이 음수입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private long quantity;

    protected MenuProduct() {

    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        validate(menu, product, quantity);
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Menu menu, Product product, long quantity) {
        return new MenuProduct(menu, product, quantity);
    }

    private void validate(Menu menu, Product product, long quantity) {
        if (Objects.isNull(menu)) {
            throw new WrongValueException(MENU);
        }

        if (Objects.isNull(product)) {
            throw new WrongValueException(PRODUCT);
        }

        if (quantity < 0) {
            throw new IllegalArgumentException(QUANTITY_IS_NEGATIVE_EXCEPTION_STATEMENT);
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity;
    }
}
