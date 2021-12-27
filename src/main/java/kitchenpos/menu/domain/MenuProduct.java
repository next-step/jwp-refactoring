package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.domain.Price;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Long menuId;

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long menuId, Long productId, long quantity) {
        this.menuId = menuId;
        this.product = Product.of(productId);
        this.quantity = Quantity.of(quantity);
    }

    private MenuProduct(Long productId, long quantity) {
        this.product = Product.of(productId);
        this.quantity = Quantity.of(quantity);
    }

    public static MenuProduct of(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public static MenuProduct of(Long menuId, Long productId, long quantity) {
        return new MenuProduct(menuId, productId, quantity);
    }

    public Price calculateProductsPrice() {
        return Price.multiply(product, quantity);
    }

    public Long getId() {
        return id;
    }

    void setMenuId(Long menuId) {
        this.menuId = menuId;
    }

    public Long getProductId() {
        return product.getId();
    }

    public long getQuantityValue() {
        return quantity.getValue();
    }

    public void fillProduct(Product product) {
        this.product = product;
    }

}
