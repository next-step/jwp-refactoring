package kitchenpos.menu.domain.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.menu.domain.value.Quantity;
import kitchenpos.product.domain.entity.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = Quantity.of(quantity);
    }

    public MenuProduct(Long seq, Product product, long quantity) {
        this.seq = seq;
        this.product = product;
        this.quantity = Quantity.of(quantity);
    }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = Quantity.of(quantity);
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

    public void toMenu(Menu menu) {
        this.menu = menu;
    }
}
