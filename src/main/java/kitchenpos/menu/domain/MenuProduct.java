package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuProduct {
    public static final String MENU_NULL_EXCEPTION_MESSAGE = "메뉴는 필수입니다.";
    public static final String PRODUCT_NULL_EXCEPTION_MESSAGE = "메뉴는 필수입니다.";
    public static final String QUANTITY_NULL_EXCEPTION_MESSAGE = "갯수는 필수입니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Product product;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, Long quantity) {
        validate(menu, product, quantity);
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    private static void validate(Menu menu, Product product, Long quantity) {
//        if (Objects.isNull(menu)) {
//            throw new IllegalArgumentException(MENU_NULL_EXCEPTION_MESSAGE);
//        }
        if (Objects.isNull(product)) {
            throw new IllegalArgumentException(PRODUCT_NULL_EXCEPTION_MESSAGE);
        }
        if (Objects.isNull(quantity)) {
            throw new IllegalArgumentException(QUANTITY_NULL_EXCEPTION_MESSAGE);
        }
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public Product getProduct() {
        return this.product;
    }
}
