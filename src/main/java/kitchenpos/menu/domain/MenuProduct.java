package kitchenpos.menu.domain;

import org.springframework.context.annotation.EnableAspectJAutoProxy;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Objects;

@Table(name = "menu_product")
@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Product product, final long quantity) {
        this(null, product, quantity);
    }

    public MenuProduct(final Long id, final Product product, final long quantity) {
        this.id = id;
        this.product = product;
        this.quantity = quantity;
    }

    public void addedBy(final Menu menu) {
        this.menu = menu;
    }

    public BigDecimal menuProductPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public BigDecimal productPrice() {
        return this.product.getPrice();
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(id, that.id)
                && Objects.equals(product, that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, product);
    }
}
