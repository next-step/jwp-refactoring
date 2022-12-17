package kitchenpos.menu.domain;

import kitchenpos.product.domain.Price;
import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {

    }

    public MenuProduct(Menu menu, Product product, Quantity quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Product product, Long quantity) {
        return new MenuProduct(null, product, Quantity.of(quantity));
    }

    public Long getProductId() {
        return this.product.getId();
    }

    public Quantity getQuantity() {
        return this.quantity;
    }

    public Price getPrice() {
        return this.product.getPrice().multiplyQuantity(this.quantity);
    }

    public void changeMenu(Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        MenuProduct that = (MenuProduct) o;

        if (!Objects.equals(seq, that.seq)) return false;
        if (!Objects.equals(menu, that.menu)) return false;
        return Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        int result = seq != null ? seq.hashCode() : 0;
        result = 31 * result + (menu != null ? menu.hashCode() : 0);
        result = 31 * result + (product != null ? product.hashCode() : 0);
        return result;
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
                "seq=" + seq +
                ", menu=" + menu +
                ", product=" + product +
                ", quantity=" + quantity +
                '}';
    }
}
