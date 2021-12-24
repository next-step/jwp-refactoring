package kitchenpos.menu.domain;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.product.domain.Amount;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;

    @OneToOne(fetch = LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private Long quantity;

    public static MenuProduct of(Menu menu, Product product, Long quantity) {
        return new MenuProduct(null, menu, product, quantity);
    }

    public static MenuProduct of(Long id, Menu menu, Product product, Long quantity) {
        return new MenuProduct(id, menu, product, quantity);
    }

    private MenuProduct(Long id, Menu menu, Product product, Long quantity) {
        this.id = id;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    protected MenuProduct() {
    }

    public Amount multiply() {
        return product.multiply(quantity);
    }

    public Long getId() {
        return id;
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
