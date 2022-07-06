package kitchenpos.domain.menu;

import java.math.BigDecimal;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "menu_id",
        foreignKey = @ForeignKey(name = "FK_MENU_PRODUCT_TO_MENU"),
        nullable = false
    )
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "product_id",
        foreignKey = @ForeignKey(name = "FK_MENU_PRODUCT_TO_PRODUCT"),
        nullable = false
    )
    private Product product;

    private long quantity;

    protected MenuProduct() {

    }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal multiplyQuantityToPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
