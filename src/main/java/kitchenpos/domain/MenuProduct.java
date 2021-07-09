package kitchenpos.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @JoinColumn(name = "menu_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @JoinColumn(name = "product_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long seq, Menu menu, Product product, Long quantity) {
        this(menu, product, quantity);
        this.seq = seq;
    }

    public MenuProduct(Menu menu, Product product, Long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public void setMenu(Menu menu) {
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

    public Quantity getQuantity() {
        return quantity;
    }
}
