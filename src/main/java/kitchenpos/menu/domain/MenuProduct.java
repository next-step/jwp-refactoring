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

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @OneToOne(fetch = FetchType.LAZY)
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {

    }

    public MenuProduct(Long seq, Menu menu, Product product, Quantity quantity) {
        this(product, quantity);
        this.seq = seq;
        this.menu = menu;
    }

    private MenuProduct(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Long seq, Menu menu, Product product, Quantity quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }

    public static MenuProduct of(Product product, Quantity quantity) {
        return new MenuProduct(product, quantity);
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

    public BigDecimal getProductPrice() {
        BigDecimal price = product.getPrice();

        return price.multiply(BigDecimal.valueOf(quantity.getQuantity()));
    }

    public void toMenu(Menu menu) {
        this.menu = menu;
    }
}
