package kitchenpos.menu.domain;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kitchenpos.product.domain.Amount;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long productId;

    private Long quantity;

    public static MenuProduct of(Long productId, Long quantity) {
        return new MenuProduct(null, productId, quantity);
    }

    public static MenuProduct of(Long id, Long productId, Long quantity) {
        return new MenuProduct(id, productId, quantity);
    }

    private MenuProduct(Long id, Long productId, Long quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    protected MenuProduct() {
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }
}
