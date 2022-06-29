package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.product.domain.ProductPrice;

@Entity
@Table(name = "menu_product")
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
    private Menu menu;
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
    private Long productId;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {}

    private MenuProduct(Long seq, Menu menu, Long productId, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = Quantity.from(quantity);
    }

    private MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = Quantity.from(quantity);
    }

    public static MenuProduct of(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public static MenuProduct of(Long seq, Menu menu, Long productId, long quantity) {
        return new MenuProduct(seq, menu, productId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return this.menu.getId();
    }

    public Long getProductId() {
        return this.productId;
    }

    public long findQuantity() {
        return quantity.getValue();
    }

    public void mappedByMenu(Menu menu) {
        this.menu = menu;
    }

    public BigDecimal calculateTotalPrice(ProductPrice price) {
        return price.getValue().multiply(BigDecimal.valueOf(this.findQuantity()));
    }
}
