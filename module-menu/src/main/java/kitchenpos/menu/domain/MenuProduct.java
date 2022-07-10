package kitchenpos.menu.domain;

import kitchenpos.domain.Quantity;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    private Menu menu;

    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {}

    public MenuProduct(Long seq, Menu menu, Long productId, Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Menu menu, Long productId, Quantity quantity) {
        this(null, menu, productId, quantity);
    }

    public MenuProduct(Long productId, Quantity quantity) {
        this(null, null, productId, quantity);
    }

    public void associateMenu(Menu menu) {
        this.menu = menu;
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
