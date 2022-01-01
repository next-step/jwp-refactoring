package kitchenpos.menu.domain;

import common.domain.Quantity;
import kitchenpos.product.exception.EmptyProductException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
    private Menu menu;

    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(final Long seq, final Menu menu, final Long productId, final Quantity quantity) {
        validateCreate(productId);
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(final Long seq, final Menu menu, final Long productId, final long quantity) {
        return new MenuProduct(seq, menu, productId, Quantity.valueOf(quantity));
    }

    public static MenuProduct of(final Menu menu, final Long productId, final long quantity) {
        return new MenuProduct(null, menu, productId, Quantity.valueOf(quantity));
    }

    public static MenuProduct of(final Long productId, final long quantity) {
        return new MenuProduct(null, null, productId, Quantity.valueOf(quantity));
    }

    private void validateCreate(final Long productId) {
        if (Objects.isNull(productId)) {
            throw new EmptyProductException();
        }
    }

    public void decideMenu(final Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return this.seq;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Long getProductId() {
        return this.productId;
    }

    public Quantity getQuantity() {
        return this.quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}