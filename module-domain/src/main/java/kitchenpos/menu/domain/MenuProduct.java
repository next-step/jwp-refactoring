package kitchenpos.menu.domain;

import kitchenpos.exception.NotFoundProductException;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "menu_product")
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "product_id")
    private Long productId;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, Long productId, long quantity) {
        this(productId, quantity);
        this.seq = seq;
    }

    public MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal totalPrice(List<Product> products) {
        return products.stream()
                .filter(this::equalsProduct)
                .findFirst()
                .orElseThrow(NotFoundProductException::new)
                .getPrice()
                .multiply(BigDecimal.valueOf(quantity));
    }

    public boolean equalsProduct(Product product) {
        return Objects.equals(productId, product.getId());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return quantity == that.quantity
                && Objects.equals(seq, that.seq)
                && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, productId, quantity);
    }
}
