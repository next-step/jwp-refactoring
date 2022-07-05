package kitchenpos.menus.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @Column(nullable = false)
    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, Long productId, long quantity) {
        this.menu = menu;
        this.productId = productId;
        this.quantity = new Quantity(quantity);
    }

    public MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = new Quantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public void registerMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public long getQuantity() {
        return quantity.getValue();
    }
}
