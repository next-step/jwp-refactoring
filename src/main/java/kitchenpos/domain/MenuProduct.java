package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    private Long productId;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public void toMenu(Menu menu) {
        if (menu == null) {
            throw new IllegalArgumentException();
        }
        if (this.menu != null) {
            this.menu.getMenuProducts().getMenuProducts().remove(this);
        }
        this.menu = menu;
        menu.getMenuProducts().getMenuProducts().add(this);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
