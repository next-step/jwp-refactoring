package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long seq;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
