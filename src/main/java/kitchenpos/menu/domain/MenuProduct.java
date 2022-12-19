package kitchenpos.menu.domain;

import kitchenpos.exception.ErrorMessage;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class MenuProduct {
    private static final int MIN_NUM = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;
    private long quantity;

    protected MenuProduct() {}

    public MenuProduct(Menu menu, Product product, long quantity) {
        validate(menu, product, quantity);
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    private void validate(Menu menu, Product product, long quantity) {
        if(Objects.isNull(menu)) {
            throw new IllegalArgumentException(ErrorMessage.MENU_PRODUCT_REQUIRED_MENU.getMessage());
        }
        if(Objects.isNull(product)) {
            throw new IllegalArgumentException(ErrorMessage.MENU_PRODUCT_REQUIRED_PRODUCT.getMessage());
        }
        if(quantity < MIN_NUM) {
            throw new IllegalArgumentException(ErrorMessage.MENU_PRODUCT_INVALID_QUANTITY.getMessage());
        }
    }

    public void updateMenu(Menu menu) {
        if(this.menu != menu) {
            this.menu = menu;
            menu.addMenuProduct(this);
        }
    }

    public BigDecimal calculateAmount() {
        return this.product.calculateAmount(quantity);
    }

    public Long getId() {
        return id;
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
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return quantity == that.quantity && Objects.equals(menu, that.menu) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menu, product, quantity);
    }
}
