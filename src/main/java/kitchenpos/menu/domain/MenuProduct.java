package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuProductExceptionCode;
import kitchenpos.utils.NumberUtil;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private long quantity;

    protected MenuProduct() {}

    public MenuProduct(Menu menu, Product product, long quantity) {
        validate(menu, product, quantity);

        updateMenu(menu);
        this.product = product;
        this.quantity = quantity;
    }

    private void validate(Menu menu, Product product, long quantity) {
        if(Objects.isNull(menu)) {
            throw new IllegalArgumentException(MenuProductExceptionCode.REQUIRED_MENU.getMessage());
        }

        if(Objects.isNull(product)) {
            throw new IllegalArgumentException(MenuProductExceptionCode.REQUIRED_PRODUCT.getMessage());
        }

        if(NumberUtil.isNotPositiveNumber(quantity)) {
            throw new IllegalArgumentException(MenuProductExceptionCode.INVALID_QUANTITY.getMessage());
        }
    }

    void updateMenu(Menu menu) {
        if(this.menu != menu) {
            this.menu = menu;
            menu.addMenuProduct(this);
        }
    }

    public BigDecimal calculateAmount() {
        return this.product.calculateAmount(quantity);
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

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        MenuProduct that = (MenuProduct) o;
        return Objects.equals(menu, that.menu) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menu, product);
    }
}
