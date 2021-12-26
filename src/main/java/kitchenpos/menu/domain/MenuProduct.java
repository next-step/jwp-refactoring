package kitchenpos.menu.domain;

import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.product.domain.Product;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Quantity quantity;

    public MenuProduct() {
    }

    private MenuProduct(Long seq, Menu menu, Product product, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = Quantity.of(quantity);
    }

    public static MenuProduct of(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }

    public static MenuProduct of(Product product, int quantity) {
        return new MenuProduct(null, null, product, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public void setMenuId(final Long menuId) {
        menu.setId(menuId);
    }

    public Long getProductId() {
        return product.getId();
    }

    public void setProductId(final Long productId) {
        product.setId(productId);
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }

    public void setQuantity(final long quantity) {
        this.quantity = Quantity.of(quantity);
    }

    public Price calculate() {
        return product.multiply(quantity.getQuantity());
    }
}
