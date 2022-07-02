package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;

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
    private int quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, Product product, int quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = quantity;
    }

    private MenuProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Product product, int quantity){
        return new MenuProduct(product, quantity);
    }

    public static MenuProduct of(Long seq, Product product, int quantity) {
        return new MenuProduct(seq, product, quantity);
    }

    public void registerMenu(Menu menu) {
        this.menu = menu;
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

    public int getQuantity() {
        return quantity;
    }
}
