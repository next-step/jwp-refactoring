package kitchenpos.menu.domain;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.common.domain.Quantity;

@Entity
@Table
public class MenuProduct {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @AttributeOverride(name = "value", column = @Column(name = "quantity", nullable = false))
    private Quantity quantity;

    protected MenuProduct() { }

    public MenuProduct(Long productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long productId, long quantity) {
        this(productId, Quantity.of(quantity));
    }

    public Long getId() {
        return id;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    void toMenu(Menu menu) {
        this.menu = menu;
    }

    boolean matchProductId(Long productId) {
        return this.productId.equals(productId);
    }
}
