package kitchenpos.menu.domain;

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
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.exception.MenuProductQuantityNegativeException;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
    private Product product;

    @Embedded
    @Column(nullable = false)
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, Long quantity) {
        validationQuantity(quantity);
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public MenuProduct(Long menuId, Product product, Long quantity) {
        validationQuantity(quantity);
        this.menu = new Menu(menuId);
        this.product = product;
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

    public Product getProduct() {
        return product;
    }

    public Price getPrice() {
        return product.multiplyPrice(quantity);
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void toMenu(Menu menu) {
        this.menu = menu;
    }
}
