package domain.menu;

import common.valueobject.Quantity;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(name = "menu_id")
    private Long menuId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, Long menuId, Product product, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.product = product;
        this.quantity = Quantity.of(quantity);
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(null, null, product, quantity);
    }

    public static MenuProduct of(long quantity) {
        return new MenuProduct(null, null, null, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void registerMenu(Long menuId) {
        this.menuId = menuId;
    }

    public BigDecimal getCalculatedPrice() {
        return product.getPrice().calculateByQuantity(quantity);
    }
}
