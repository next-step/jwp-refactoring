package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Menu menu, Product product, Quantity quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
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

    public Quantity getQuantity() {
        return quantity;
    }

    public void matchMenu(Menu menu) {
        this.menu = menu;
    }
}
