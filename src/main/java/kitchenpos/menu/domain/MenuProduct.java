package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.exception.MenuProductQuantityNegativeException;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Embedded
    @Column(nullable = false)
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, Long quantity) {
        validationQuantity(quantity);
        this.productId = productId;
        this.quantity = new Quantity(quantity);
    }

    public MenuProduct(Long menuId, Long productId, Long quantity) {
        validationQuantity(quantity);
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = new Quantity(quantity);
    }

    private void validationQuantity(Long quantity) {
        if (quantity < 0) {
            throw new MenuProductQuantityNegativeException();
        }
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

    public Quantity getQuantity() {
        return quantity;
    }

}
