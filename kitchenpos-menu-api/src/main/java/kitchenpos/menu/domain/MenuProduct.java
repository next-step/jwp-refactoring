package kitchenpos.menu.domain;

import kitchenpos.menu.exception.MenuProductExceptionCode;

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

    @Embedded
    private MenuProductQuantity quantity;

    protected MenuProduct() {}

    public MenuProduct(Menu menu, Product product, long quantity) {
        validate(menu, product);

        updateMenu(menu);
        this.product = product;
        this.quantity = new MenuProductQuantity(quantity);
    }

    private void validate(Menu menu, Product product) {
        if(Objects.isNull(menu)) {
            throw new IllegalArgumentException(MenuProductExceptionCode.REQUIRED_MENU.getMessage());
        }

        if(Objects.isNull(product)) {
            throw new IllegalArgumentException(MenuProductExceptionCode.REQUIRED_PRODUCT.getMessage());
        }
    }

    void updateMenu(Menu menu) {
        if(this.menu != menu) {
            this.menu = menu;
            menu.addMenuProduct(this);
        }
    }

    public BigDecimal calculateAmount() {
        return this.product.calculateAmount(getQuantity());
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
        return quantity.getQuantity();
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
