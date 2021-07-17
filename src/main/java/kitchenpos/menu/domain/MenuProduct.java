package kitchenpos.menu.domain;

import kitchenpos.common.Quantity;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
    private Menu menu;

    @JoinColumn(name = "product_id", nullable = false)
    private Long productId;

    @Embedded
    Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long productId, Quantity quantity) {
        this(0L, null, productId, quantity);
    }

    public MenuProduct(Menu menu, Long productId, Quantity quantity) {
        this(null, menu, productId, quantity);
    }

    public MenuProduct(Long seq, Menu menu, Long productId, Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public boolean isMenuProductOf(Menu menu) {
        return this.menu.equals(menu);
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
