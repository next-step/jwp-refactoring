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
    @JoinColumn(name = "product_id")
    private Long productId;
    private int quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, Long productId, int quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    private MenuProduct(Long productId, int quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(Long productId, int quantity){
        return new MenuProduct(productId, quantity);
    }

    public static MenuProduct of(Long seq, Long productId, int quantity) {
        return new MenuProduct(seq, productId, quantity);
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

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}
