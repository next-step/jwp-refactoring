package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
public class MenuProduct {
    private static final String INVALID_MENU = "메뉴 상품을 만들기 위해서는 메뉴가 존재해야 합니다.";
    private static final String INVALID_PRODUCT = "메뉴 상품을 만들기 위해서는 상품이 존재해야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        validateMenu(menu);
        validateProduct(product);
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    private void validateMenu(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException(INVALID_MENU);
        }
    }

    private void validateProduct(Product product) {
        if (Objects.isNull(product)) {
            throw new IllegalArgumentException(INVALID_PRODUCT);
        }
    }

    public Long id() {
        return id;
    }

    public Menu menu() {
        return menu;
    }

    public BigDecimal price(long quantity) {
        return product.price().multiply(BigDecimal.valueOf(quantity));
    }

    public Product product() {
        return product;
    }

    public long quantity() {
        return quantity;
    }

}
