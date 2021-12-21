package kitchenpos.menu.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private long quantity;

    public MenuProduct() {
    }

    private MenuProduct(Long menuId, Long productId, long quantity) {
        this.menu = Menu.of(menuId);
        this.product = Product.of(productId);
        this.quantity = quantity;
    }

    public MenuProduct(Long productId, long quantity) {
        this.product = Product.of(productId);
        this.quantity = quantity;
    }

    public MenuProduct(Long id, Long menuId, Long productId, long quantity) {
        this.id = id;
        this.menu = Menu.of(menuId);
        this.product = Product.of(productId);
        this.quantity = quantity;
    }

    public static MenuProduct of(Long productId, long quantity) {
        return new MenuProduct(productId, quantity);
    }

    public static MenuProduct of(Long menuId, Long productId, long quantity) {
        return new MenuProduct(menuId, productId, quantity);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public void setMenu(final Long menuId) {
        this.menu = Menu.of(menuId);
    }

    public Long getProductId() {
        return product.getId();
    }

    public void setProduct(final Long productId) {
        this.product = Product.of(productId);
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(final long quantity) {
        this.quantity = quantity;
    }
}
