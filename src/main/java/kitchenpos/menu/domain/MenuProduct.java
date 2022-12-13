package kitchenpos.menu.domain;

import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Embedded
    private Quantity quantity;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    protected MenuProduct() {}

    public MenuProduct(Quantity quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public MenuProduct(Long seq, Quantity quantity, Menu menu, Product product) {
        this(quantity, product);
        this.seq = seq;
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Price calculatePrice() {
        Price price = product.getPrice();
        return price.multiply(BigDecimal.valueOf(quantity.value()));
    }
}
