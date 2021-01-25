package kitchenpos.menu.domain;

import kitchenpos.common.exception.NotFoundEntityException;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    private long quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long menuId, Long productId, long quantity) {
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public MenuProduct addMenuId(Long menuId) {
        return new MenuProduct(menuId, this.productId, this.quantity);
    }

    public Product findProduct(List<Product> products) {
        return products.stream()
                .filter(product -> product.equalsId(productId))
                .findFirst()
                .orElseThrow(() -> new NotFoundEntityException("등록되지 않은 상품입니다."));
    }

    public BigDecimal calculatePrice(Product product) {
        return product.multiplyQuantity(BigDecimal.valueOf(quantity));
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
