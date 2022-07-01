package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(nullable = false)
    private Quantity quantity;
    @Column(name = "product_id", nullable = false)
    private Long productId;

    protected MenuProduct() {
    }

    public MenuProduct(Quantity quantity, Long productId) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public Price getTotalPrice(Product product) {
        Price productPrice = product.getPrice();
        return new Price(productPrice.getPrice() * quantity.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Long getProductId() {
        return productId;
    }
}
