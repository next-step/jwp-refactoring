package kitchenpos.menu.domain;


import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, Long quantity) {
        this.productId = productId;
        this.quantity = Quantity.of(quantity);
    }

    public Long getId() {
        return id;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity.value();
    }

    public BigDecimal price(BigDecimal productPrice) {
        return productPrice.multiply(BigDecimal.valueOf(quantity.value()));
    }

}
