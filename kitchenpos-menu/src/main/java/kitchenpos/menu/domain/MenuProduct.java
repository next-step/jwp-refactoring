package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.common.exception.InvalidParameterException;

@Entity
public class MenuProduct {
    private static final String ERROR_MESSAGE_MENU_PRODUCT_PRODUCT_IS_NULL = "상품은 필수입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(nullable = false)
    private Long productId;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {}

    private MenuProduct(Long seq, Long productId, long quantity) {
        validate(productId);
        this.seq = seq;
        this.productId = productId;
        this.quantity = Quantity.from(quantity);
    }

    private MenuProduct(Long productId, long quantity) {
        this(null, productId, quantity);
    }

    private void validate(Long productId) {
        if (productId == null) {
            throw new InvalidParameterException(ERROR_MESSAGE_MENU_PRODUCT_PRODUCT_IS_NULL);
        }
    }

    public static MenuProduct of(Long seq, Long productId, long quantity) {
        return new MenuProduct(seq, productId, quantity);
    }

    public static MenuProduct of(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public Price price(Price price) {
        return quantity.multiply(price);
    }

    public Long seq() {
        return seq;
    }

    public Long productId() {
        return productId;
    }

    public Quantity quantity() {
        return quantity;
    }

    public long quantityValue() {
        return quantity.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq)
                && Objects.equals(productId, that.productId)
                && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, productId, quantity);
    }
}
