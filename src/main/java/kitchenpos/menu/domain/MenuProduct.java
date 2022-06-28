package kitchenpos.menu.domain;

import javax.persistence.*;
import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {}

    public static MenuProduct createMenuProduct(Product product, long quantity) {
        MenuProduct menuProduct = new MenuProduct();
        menuProduct.updateProduct(product);
        menuProduct.updateQuantity(quantity);
        return menuProduct;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getProductId() {
        return product.getId();
    }

    public long getQuantity() {
        return quantity;
    }

    public void addMenu(Menu menu) {
        this.menu = menu;
    }

    private void updateQuantity(long quantity) {
        this.quantity = quantity;
    }

    private void updateProduct(Product product) {
        this.product = product;
    }

    public Price getProductPrice() {
        Price price = product.getPrice();
        price.multiply(quantity);
        return price;
    }
}
