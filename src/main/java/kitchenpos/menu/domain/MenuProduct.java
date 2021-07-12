package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Table(name = "menu_product")
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public long getProductId() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity.value();
    }

    public BigDecimal getMenuProductPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity.value()));
    }

    public void updateMenu(Menu menu) {
        this.menu = menu;
    }
}
