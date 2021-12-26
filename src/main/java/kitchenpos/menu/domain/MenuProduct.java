package kitchenpos.menu.domain;

import java.util.Objects;

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
    
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    
    private long quantity;
    
    protected MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(product, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
    
    public Price calculatePrice() {
        return product.getPrice().multiply(this.quantity);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return quantity == that.quantity && Objects.equals(menu, that.menu) && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menu, product, quantity);
    }
}
