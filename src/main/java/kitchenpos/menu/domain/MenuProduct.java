package kitchenpos.menu.domain;

import kitchenpos.common.Quantity;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class MenuProduct {
    public static final String PRODUCT_NULL_EXCEPTION_MESSAGE = "메뉴는 필수입니다.";
    public static final String QUANTITY_NULL_EXCEPTION_MESSAGE = "갯수는 필수입니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Product product;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, Quantity quantity) {
        validate(product, quantity);
        this.product = product;
        this.quantity = quantity;
    }

    private static void validate(Product product, Quantity quantity) {
        validateNullProduct(product);
        validateNullQuantity(quantity);
    }

    private static void validateNullQuantity(Quantity quantity) {
        if (Objects.isNull(quantity)) {
            throw new IllegalArgumentException(QUANTITY_NULL_EXCEPTION_MESSAGE);
        }
    }

    private static void validateNullProduct(Product product) {
        if (Objects.isNull(product)) {
            throw new IllegalArgumentException(PRODUCT_NULL_EXCEPTION_MESSAGE);
        }
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return this.product;
    }

    public long getQuantity() {
        return this.quantity.getQuantity();
    }

    public void mapMenu(Menu menu) {
        this.menu = menu;
    }
}
