package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private Menu menu;
    @ManyToOne
    private Product product;
    private long quantity;

    protected MenuProduct(){}

    public MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public Long getId() {
        return id;
    }

    public long getPrice() {
        return product.getPrice() * quantity;
    }
}
