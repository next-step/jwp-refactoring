package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long menuId;

    @Column
    private Long productId;

    @Column
    private long quantity;

    public MenuProduct() { }

    public MenuProduct(Long id, Long menuId, Long productId, long quantity) {
        this.id = id;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public Long getMenuId() {
        return menuId;
    }

        public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }
}
