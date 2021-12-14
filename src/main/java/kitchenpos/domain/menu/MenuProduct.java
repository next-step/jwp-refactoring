package kitchenpos.domain.menu;

import javax.persistence.Entity;
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

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private long quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(product, quantity);
    }

    public static MenuProduct of(Menu menu, Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct(product, quantity);
        menuProduct.acceptMenu(menu);

        return menuProduct;
    }
    
    public Long getSeq() {
        return this.seq;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Product getProduct() {
        return this.product;
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
