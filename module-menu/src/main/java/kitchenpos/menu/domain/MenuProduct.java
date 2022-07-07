package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;

import javax.persistence.*;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {

    }

    public MenuProduct(Long seq, Menu menu, Long productId, Quantity quantity) {
        this(productId, quantity);
        this.seq = seq;
        this.menu = menu;
    }

    private MenuProduct(Long productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(Long seq, Menu menu, Long productId, Quantity quantity) {
        return new MenuProduct(seq, menu, productId, quantity);
    }

    public static MenuProduct of(Long productId, Quantity quantity) {
        return new MenuProduct(productId, quantity);
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

    public long getQuantity() {
        return quantity.getQuantity();
    }

    public void toMenu(Menu menu) {
        this.menu = menu;
    }
}
