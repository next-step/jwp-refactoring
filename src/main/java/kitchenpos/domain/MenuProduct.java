package kitchenpos.domain;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id_v2", foreignKey = @ForeignKey(name = "fk_menu_product_menu_v2"), nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id_v2", foreignKey = @ForeignKey(name = "fk_menu_product_product_v2"), nullable = false)
    private Product product;

    @Column(nullable = false)
    private long quantity;

    @Deprecated
    private Long menuId;

    @Deprecated
    private Long productId;

    protected MenuProduct() {
    }

    private MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.productId = productId;
        this.quantity = quantity;
    }

    private MenuProduct(Long seq, Long menuId, Product product, long quantity) {
        this.seq = seq;
        this.menuId = menuId;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Long seq, Long menuId, Long productId, long quantity) {
        return new MenuProduct(seq, menuId, productId, quantity);
    }

    public static MenuProduct of(Long productId, long quantity) {
        return new MenuProduct(null, null, productId, quantity);
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(null, null, product, quantity);
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

    public BigDecimal amount() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public void setup(Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProduct that = (MenuProduct) o;
        return seq.equals(that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
