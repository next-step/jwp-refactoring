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
    @JoinColumn(name = "id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "id")
    private Product product;

    private long quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Menu menu, Product product, long quantity) {
        return new MenuProduct(menu, product, quantity);
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

    public void changeMenu(Menu menu) {
        this.menu = menu;
    }
}
