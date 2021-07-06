package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Menu menu;
//    @Column
//    private Long menuId;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    private Product product;
//    @Column
//    private Long productId;

    @Column
    private long quantity;

    public MenuProduct() { }

    public MenuProduct(Long id, Menu menu, Product product, long quantity) {
        this.id = id;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public MenuProduct(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
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

    public long getQuantity() {
        return quantity;
    }

}
