package kitchenpos.domain.menu;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.product.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "productId", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    @Column(name = "quantity")
    private Long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    @Deprecated
    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    @Deprecated
    public Long getMenuId() {
        return this.menu.getId();
    }

    @Deprecated
    public void setMenuId(final Long menuId) {
        this.menu.setId(menuId);
    }

    public Long getProductId() {
        return product.getId();
    }

    @Deprecated
    public void setProductId(final Long productId) {
        this.product.setId(productId);
    }

    public long getQuantity() {
        return quantity;
    }

    @Deprecated
    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return product.calculatePrice(quantity);
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public MenuProduct mapMaenu(Menu menu) {
        this.setMenu(menu);
        return this;
    }
}
