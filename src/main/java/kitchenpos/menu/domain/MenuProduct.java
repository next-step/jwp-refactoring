package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
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

    private MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(product, quantity);
    }

    public BigDecimal calculatePrice() {
        return product.multiplyQuantity(BigDecimal.valueOf(quantity));
    }

    public void addMenu(Menu menu) {
        this.menu = menu;
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

    public void setProduct(final Product product) {
        this.product = product;
    }
}
