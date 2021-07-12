package kitchenpos.menu.domain;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    private Long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void registerMenu(Menu menu) {
        this.menu = menu;
    }

    public BigDecimal getPriceByQuantity() {
        return product.getPriceByQuantity(quantity);
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

    public Long getQuantity() {
        return quantity;
    }
}
