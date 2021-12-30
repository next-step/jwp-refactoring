package kitchenpos.domain;

import kitchenpos.common.exceptions.ProductRequiredException;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
    private Product product;

    @Embedded
    @AttributeOverride(name = "quantity", column = @Column(name = "quantity", nullable = false))
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(final Long seq, final Menu menu, final Product product, final Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(final Long seq, final Menu menu, final Product product, final Long quantity) {
        return new MenuProduct(seq, menu, product, Quantity.valueOf(quantity));
    }

    public static MenuProduct of(final Menu menu, final Product product, final Long quantity) {
        return new MenuProduct(null, menu, product, Quantity.valueOf(quantity));
    }

    public static MenuProduct of(final Product product, final Long quantity) {
        validateCreate(product);
        return new MenuProduct(null, null, product, Quantity.valueOf(quantity));
    }

    private static void validateCreate(final Product product) {
        if (Objects.isNull(product)) {
            throw new ProductRequiredException();
        }
    }

    public void decideMenu(final Menu menu) {
        this.menu = menu;
    }

    public Price getTotalPrice() {
        return product.getPrice().multiply(quantity.toLong());
    }

    public Long getSeq() {
        return this.seq;
    }

    public Menu getMenu() {
        return this.menu;
    }

    public Product getProduct() {
        return this.product;
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