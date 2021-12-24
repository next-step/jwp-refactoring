package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Product product, Long quantity) {
        this.quantity = Quantity.valueOf(quantity);
        setProduct(product);
    }

    public static MenuProduct of(Product product, Long quantity) {
        return new MenuProduct(product, quantity);
    }

    public boolean equalMenuProduct(MenuProduct other) {
        return product.equals(other.product) && quantity.equals(other.quantity);
    }

    public Price getPrice() {
        return product.multiplyQuantity(quantity);
    }

    public Product getProduct() {
        return this.product;
    }

    private void setProduct(Product product) {
        validateProduct(product);
        this.product = product;
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity.toLong();
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new InvalidArgumentException("상품은 필수입니다.");
        }
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
        return seq.equals(that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
