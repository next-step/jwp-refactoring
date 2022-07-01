package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {}

    private MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct createMenuProduct(Long productId, long quantity) {
       return new MenuProduct(productId, quantity);
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public void addMenu(Menu menu) {
        this.menu = menu;
    }
}
