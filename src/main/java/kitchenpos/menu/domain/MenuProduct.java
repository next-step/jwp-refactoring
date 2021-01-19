package kitchenpos.menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id", nullable = false)
    private Long menuId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long menu, Product product, long quantity) {
        validateNotGreaterThanZero(quantity);
        this.menuId = menu;
        this.product = product;
        this.quantity = quantity;
    }

    private void validateNotGreaterThanZero(long quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("수량은 0보다 커야합니다.");
        }
    }

    public BigDecimal getPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Long getMenuId() {
        return menuId;
    }
}
