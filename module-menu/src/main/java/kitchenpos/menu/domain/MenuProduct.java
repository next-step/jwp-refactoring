package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table
public class MenuProduct {
    public static final String PRODUCT_NULL_EXCEPTION_MESSAGE = "상품은 필수입니다.";
    public static final String QUANTITY_NULL_EXCEPTION_MESSAGE = "갯수는 필수입니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    private Long productId;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, Quantity quantity) {
        validate(productId, quantity);
        this.productId = productId;
        this.quantity = quantity;
    }

    private static void validate(Long productId, Quantity quantity) {
        validateNullProduct(productId);
        validateNullQuantity(quantity);
    }

    private static void validateNullProduct(Long productId) {
        if (Objects.isNull(productId)) {
            throw new IllegalArgumentException(PRODUCT_NULL_EXCEPTION_MESSAGE);
        }
    }

    private static void validateNullQuantity(Quantity quantity) {
        if (Objects.isNull(quantity)) {
            throw new IllegalArgumentException(QUANTITY_NULL_EXCEPTION_MESSAGE);
        }
    }

    public Long getId() {
        return id;
    }

    public long getQuantity() {
        return this.quantity.getQuantity();
    }

    public void mapMenu(Menu menu) {
        this.menu = menu;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Long getProductId() {
        return this.productId;
    }
}
