package kitchenpos.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Product product;

    private Long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, Long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    // for test
    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
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

    public BigDecimal getPrice() {
        return product.getPrice(quantity);
    }
}
