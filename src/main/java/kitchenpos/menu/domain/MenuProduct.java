package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long productId;
    private long quantity;
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
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

    public Menu getMenu() {
        return menu;
    }

    public void saveMenu(Menu menu) {
        this.menu = menu;
    }
}
