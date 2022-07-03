package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long menuId, Long productId, long quantity) {
        this.menu = Menu.of(menuId);
        this.productId = productId;
        this.quantity = Quantity.from(quantity);
    }

    private MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = Quantity.from(quantity);
    }

    public static MenuProduct of(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public static MenuProduct of(Long menuId, Long productId, long quantity) {
        return new MenuProduct(menuId, productId, quantity);
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

    public long getQuantityValue() {
        return quantity.getValue();
    }

    void bindMenu(Menu menu) {
        this.menu = menu;
    }
}
