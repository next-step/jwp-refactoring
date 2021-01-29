package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private Long menuId;
    @ManyToOne(fetch = FetchType.LAZY)
    private Long productId;
    private int quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long menuId, Long productId, int quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void changeMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
