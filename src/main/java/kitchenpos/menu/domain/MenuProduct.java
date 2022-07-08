package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Quantity;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(MenuProduct menuProduct) {
        this(menuProduct.productId, menuProduct.quantity);
    }

    private MenuProduct(Long productId, Long quantity) {
        this(productId, Quantity.from(quantity));
    }

    private MenuProduct(Long productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
        validateNonNullFields();
    }

    public static MenuProduct from(MenuProduct menuProduct) {
        return new MenuProduct(menuProduct);
    }

    public static MenuProduct of(Long productId, Long quantity) {
        return new MenuProduct(productId, quantity);
    }

    private void validateNonNullFields() {
        if (productId == null || quantity == null) {
            throw new IllegalArgumentException("상품, 수량은 메뉴 상품의 필수 사항입니다.");
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity.value();
    }
}
