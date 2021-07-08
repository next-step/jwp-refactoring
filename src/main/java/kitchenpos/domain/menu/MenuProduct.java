package kitchenpos.domain.menu;

import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.product.Product;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    private Menu menu;

    @ManyToOne
    private Product product;

    private Quantity quantity;

    protected MenuProduct() {
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

    public Price getAmount() {
        return product.multiplyPrice(quantity);
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public boolean isSameMenu(Menu menu) {
        return this.menu == menu;
    }
}
