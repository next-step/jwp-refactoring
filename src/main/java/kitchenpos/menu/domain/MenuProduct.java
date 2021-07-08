package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @OneToOne
    private Product product;

    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public Long seq() {
        return id;
    }

    public Menu menu() {
        return menu;
    }

    public BigDecimal price(long quantity) {
        return product.price().multiply(BigDecimal.valueOf(quantity));
    }

    public Product product() {
        return product;
    }

    public long quantity() {
        return quantity;
    }

}
