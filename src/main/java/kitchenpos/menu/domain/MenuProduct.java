package kitchenpos.menu.domain;

import java.math.BigDecimal;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @OneToOne
    @JoinColumn(name = "product_id")
    private Product product;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this(null, null, product, quantity);
    }

    public MenuProduct(Long seq, Menu menu, Product product, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }


    public Long getSeq() {
        return seq;
    }

    public Long menuId() {
        return menu.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Product getProduct() {
        return product;
    }

    public Long productId() {
        return product.getId();
    }

    public BigDecimal calculateTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
