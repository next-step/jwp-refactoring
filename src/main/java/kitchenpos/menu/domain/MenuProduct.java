package kitchenpos.menu.domain;

import java.math.BigDecimal;

import javax.persistence.*;

import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    private Long quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Transient
    private Long menuId;
    @Transient
    private Long productId;

    public MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
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
    // jdbc 제거 후 삭제
    public void setMenuId(final Long menuId) {
        this.menuId = menuId;
    }
    // jdbc 제거 후 삭제
    public void setProductId(final Long productId) {
        this.productId = productId;
    }
    // jdbc 제거 후 삭제
    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal multiplyProductPriceByQuantity() {
        return product.getPrice().multiply(BigDecimal.valueOf(this.quantity));
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }
}
