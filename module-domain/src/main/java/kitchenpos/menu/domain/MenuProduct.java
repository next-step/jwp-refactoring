package kitchenpos.menu.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class MenuProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "product_id",foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
    private Long productId;

    @Embedded
    private Quantity quantity;

    @Transient
    private Price menuProductPrice;

    protected MenuProduct() {}

    private MenuProduct(Long seq, Long productId, Quantity quantity, BigDecimal productPrice) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
        this.menuProductPrice = calculateTotalPrice(productPrice, quantity.get());
    }

    public static MenuProduct of(Long productId, Quantity quantity, BigDecimal productPrice) {
        return new MenuProduct(null, productId, quantity, productPrice);
    }

    private BigDecimal multiplyByQuantity(BigDecimal price, Long quantity) {
        return price.multiply(BigDecimal.valueOf(quantity));
    }

    private Price calculateTotalPrice(BigDecimal price, Long quantity) {
        return Price.of(multiplyByQuantity(price, quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity.get();
    }

    public BigDecimal getMenuProductPrice() {
        return menuProductPrice.get();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
