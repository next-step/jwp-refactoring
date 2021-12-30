package kitchenpos.menu.domain;

import kitchenpos.common.domain.BaseEntity;
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

    protected MenuProduct() {}

    private MenuProduct(Long seq, Long productId, Quantity quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(Long productId, Quantity quantity) {
        return new MenuProduct(null, productId, quantity);
    }

    public BigDecimal multiplyByQuantity(BigDecimal price) {
        return price.multiply(BigDecimal.valueOf(quantity.get()));
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
