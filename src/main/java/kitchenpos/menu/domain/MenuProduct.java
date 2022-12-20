package kitchenpos.menu.domain;

import kitchenpos.exception.ErrorMessage;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    @Embedded
    private MenuProductQuantity quantity;

    protected MenuProduct() {}

    public MenuProduct(Menu menu, Product product, long quantity) {
        validate(menu, product);

        this.menu = menu;
        this.product = product;
        this.quantity = new MenuProductQuantity(quantity);
    }

    private void validate(Menu menu, Product product) {
        if(Objects.isNull(menu)) {
            throw new IllegalArgumentException(ErrorMessage.MENU_PRODUCT_REQUIRED_MENU.getMessage());
        }
        if(Objects.isNull(product)) {
            throw new IllegalArgumentException(ErrorMessage.MENU_PRODUCT_REQUIRED_PRODUCT.getMessage());
        }
    }

    public void updateMenu(Menu menu) {
        if(this.menu != menu) {
            this.menu = menu;
            menu.addMenuProduct(this);
        }
    }

    public BigDecimal calculateAmount() {
        return this.product.calculateAmount(quantity.value());
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
        return quantity.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(menu, that.menu) && Objects.equals(product, that.product) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menu, product, quantity);
    }
}
