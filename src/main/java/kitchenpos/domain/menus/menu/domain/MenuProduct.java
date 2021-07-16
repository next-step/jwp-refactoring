package kitchenpos.domain.menus.menu.domain;

import kitchenpos.domain.menus.Price;
import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @Column(name = "product_id")
    private Long productId;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Menu menu, final Long productId, final long quantity) {
        this(null, menu, productId, quantity);
    }

    public MenuProduct(final Long id, final Menu menu, final Long productId, final long quantity) {
        setMenu(menu);

        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    Price calculate(Price productPrice) {
         return productPrice.times(quantity);
    }

    private void setMenu(final Menu menu) {
        this.menu = menu;
        menu.appendMenuProducts(this);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        final MenuProduct that = (MenuProduct) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
