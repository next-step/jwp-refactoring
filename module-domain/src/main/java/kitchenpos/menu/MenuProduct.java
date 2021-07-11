package kitchenpos.menu;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.product.Product;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id")
    private Long menuId;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private Long quantity;

    protected MenuProduct() {}

    public MenuProduct(Product product, Long quantity) {
        this(null, null, product, quantity);
    }

    public MenuProduct(Long seq, Product product, Long quantity) {
        this(null, null, product, quantity);
    }

    public MenuProduct(Long seq, Long menuId, Product product, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Product product) {
        this(null, null, product, 0L);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getQuantity() {
        return quantity;
    }

    public BigDecimal totalPrice() {
        return BigDecimal.valueOf(quantity).multiply(product.getPrice());
    }

    public void assginMenu(Long menuId) {
        this.menuId = menuId;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Long getProductId() {
        return product.getId();
    }

}
