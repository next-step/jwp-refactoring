package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.Price;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    private long quantity;

    protected MenuProduct() {
        // empty
    }

    public MenuProduct(final Product product, final long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void connectMenu(final Menu menu) {
        this.menu = menu;
    }

    public Price calculatePrice(final long times) {
        return this.product.multiply(times);
    }

    public long getQuantity() {
        return this.quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public Long getMenuId() {
        if (Objects.isNull(this.menu)) {
            return 0L;
        }
        return this.menu.getId();
    }

    public Long getProductId() {
        if (Objects.isNull(this.product)) {
            return 0L;
        }
        return this.product.getId();
    }
}
