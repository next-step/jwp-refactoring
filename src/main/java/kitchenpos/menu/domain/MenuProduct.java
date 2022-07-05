package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private Long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(MenuProduct menuProduct) {
        this(new Product(menuProduct.product), menuProduct.quantity);
    }

    private MenuProduct(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
        validate();
    }

    private void validate() {
        validateNonNullFields();
        validateQuantity();
    }

    private void validateNonNullFields() {
        if (product == null || quantity == null) {
            throw new IllegalArgumentException("상품, 수량은 메뉴 상품의 필수 사항입니다.");
        }
    }

    private void validateQuantity() {
        if (quantity < 0) {
            throw new IllegalArgumentException("수량이 0보다 작을 수 없습니다.");
        }
    }

    public static MenuProduct from(MenuProduct menuProduct) {
        return new MenuProduct(menuProduct);
    }

    public static MenuProduct of(Product product, Long quantity) {
        return new MenuProduct(product, quantity);
    }

    public Price amount() {
        return product.calculateTotal(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return product.getId();
    }

    public Long getQuantity() {
        return quantity;
    }
}
