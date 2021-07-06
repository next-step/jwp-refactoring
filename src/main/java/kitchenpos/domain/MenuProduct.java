package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;
    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    private Long productId;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, Menu menu, Long productId, long quantity) {
        this(menu, productId, quantity);
        this.seq = seq;
    }

    public MenuProduct(Menu menu, Long productId, long quantity) {
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setMenu(Menu menu) {
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

    public long getQuantity() {
        return quantity;
    }
}
