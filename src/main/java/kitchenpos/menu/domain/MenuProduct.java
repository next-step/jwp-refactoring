package kitchenpos.menu.domain;

import kitchenpos.common.ErrorCode;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class MenuProduct {

    private static final int ZERO = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private long quantity;

    public MenuProduct() {}

    public MenuProduct(Menu menu, Product product, long quantity) {
        validation(menu, product, quantity);
        updateMenu(menu);
        this.product = product;
        this.quantity = quantity;
    }

    public void updateMenu(Menu menu) {
        if(this.menu != menu) {
            this.menu = menu;
            menu.addMenuProduct(this);
        }
    }

    public Long getProductId() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public void setMenuId(Long menuId) {
    }

    public void setSeq(long aLong) {
    }

    public void setProductId(long product_id) {
    }

    public void setQuantity(long quantity) {
    }

    public BigDecimal calculateAmount() {
        return this.product.calculateAmount(quantity);
    }

    private void validation(Menu menu, Product product, long quantity) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_MENU.getErrorMessage());
        }
        if (Objects.isNull(product)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_PRODUCT.getErrorMessage());
        }
        if (isNegative(quantity)) {
            throw new IllegalArgumentException(ErrorCode.INVALID_FORMAT_MENU_QUANTITY.getErrorMessage());
        }
    }

    private boolean isNegative(long quantity) {
        return quantity < ZERO;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProduct() {
        return product.getId();
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
