package kitchenpos.menu.domain;

import javax.persistence.AttributeOverride;
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
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.exception.MenuProductQuantityNegativeException;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
    private Menu menu;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Embedded
    @AttributeOverride(name = "value", column = @Column(name = "quantity", nullable = false))
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, Long quantity) {
        validationQuantity(quantity);
        this.productId = productId;
        this.quantity = new Quantity(quantity);
    }

    public MenuProduct(Long menuId, Long productId, Long quantity) {
        validationQuantity(quantity);
        this.menu = new Menu(menuId);
        this.productId = productId;
        this.quantity = new Quantity(quantity);
    }

    private void validationQuantity(Long quantity) {
        if (quantity < 0) {
            throw new MenuProductQuantityNegativeException();
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void toMenu(Menu menu) {
        this.menu = menu;
    }

}
