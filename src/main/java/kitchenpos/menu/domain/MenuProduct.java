package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.common.domain.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long id, Menu menu, Product product, long quantity) {
        this.id = id;
        this.menu = menu;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public static MenuProduct of(Menu menu, Product product, long quantity) {
        return new MenuProduct(null, menu, product, quantity);
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

    public Quantity getQuantity() {
        return quantity;
    }

    public void changeMenu(Menu menu) {
        this.menu = menu;
    }

    public BigDecimal multiplyQuantityToPrice() {
        return product.multiplyQuantity(quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        MenuProduct that = (MenuProduct)o;
        return Objects.equals(id, that.id) && Objects.equals(menu, that.menu)
            && Objects.equals(product, that.product) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, menu, product, quantity);
    }
}
