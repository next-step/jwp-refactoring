package kitchenpos.vo;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import kitchenpos.domain.product.Product;

@Embeddable
public class ProductId {
    @Column(name = "product_id")
    private final Long productId;

    protected ProductId() {
        this.productId = null;
    }

    private ProductId(Long productId) {
        this.productId = productId;
    }

    public static ProductId of(Long productId) {
        return new ProductId(productId);
    }
    public static ProductId of(Product product) {
        return new ProductId(product.getId());
    }

    public Long value() {
        return this.productId;
    }
}
