package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private Long productId;
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this(null, product.getId(), quantity);
    }

    public MenuProduct(Long productId, long quantity) {
        this(null, productId, quantity);
    }

    public MenuProduct(Menu menu, Long productId, long quantity) {
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }


    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return quantity == that.quantity && Objects.equals(seq, that.seq) && Objects.equals(menu, that.menu) && Objects.equals(productId, that.productId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menu, productId, quantity);
    }
}
