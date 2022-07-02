package kitchenpos.domain.menu;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(nullable = false)
    private Long menuId;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private long quantity;

    public MenuProduct(Long menuId, Long productId, long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    protected MenuProduct() {
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }
}
