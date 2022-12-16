package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.exception.InvalidParameterException;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
    private static final String ERROR_MESSAGE_MENU_PRODUCT_PRODUCT_IS_NULL = "상품은 필수입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {}

    private MenuProduct(Long seq, Product product, long quantity) {
        validate(product);
        this.seq = seq;
        this.product = product;
        this.quantity = Quantity.from(quantity);
    }

    private MenuProduct(Product product, long quantity) {
        this(null, product, quantity);
    }

    private void validate(Product product) {
        if (product == null) {
            throw new InvalidParameterException(ERROR_MESSAGE_MENU_PRODUCT_PRODUCT_IS_NULL);
        }
    }

    public static MenuProduct of(Long seq, Product product, long quantity) {
        return new MenuProduct(seq, product, quantity);
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(product, quantity);
    }

    void changeMenu(Menu menu) {
        this.menu = menu;
    }

    public Price price() {
        return quantity.multiply(product.price());
    }

    public Long seq() {
        return seq;
    }

    public Menu menu() {
        return menu;
    }

    public Product product() {
        return product;
    }

    public Quantity quantity() {
        return quantity;
    }

    public Long menuId() {
        return menu.id();
    }

    public Long productId() {
        return product.id();
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
                && Objects.equals(product, that.product)
                && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, product, quantity);
    }
}
