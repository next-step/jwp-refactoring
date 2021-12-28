package kitchenpos.menu.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
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

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Embedded
    private ProductId productId;

    private long quantity;

    protected MenuProduct() {
    }

    private MenuProduct(ProductId productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(ProductId productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public static MenuProduct of(Menu menu, ProductId productId, long quantity) {
        MenuProduct menuProduct = new MenuProduct(productId, quantity);
        menuProduct.acceptMenu(menu);

        return menuProduct;
    }
    
    public Long getSeq() {
        return this.seq;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public ProductId getProductId() {
        return this.productId;
    }

    public long getQuantity() {
        return this.quantity;
    }

    public void acceptMenu(Menu menu) {
        if (this.menu != null) {
            this.menu.getMenuProducts().remove(this);
        }

        this.menu = menu;
        this.menu.getMenuProducts().add(this);
    }
}
