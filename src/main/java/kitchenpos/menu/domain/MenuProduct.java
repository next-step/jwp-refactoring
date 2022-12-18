package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "menu_product")
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", nullable = false, columnDefinition = "bigint(20)")
    private Long seq;
    @Column(name = "menu_id", nullable = false, columnDefinition = "bigint(20)")
    private Long menuId;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", nullable = false, columnDefinition = "bigint(20)", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, Long quantity) {
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Product getProduct() {
        return product;
    }

    public Long getQuantity() {
        return quantity.value();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuProduct)) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(menuId, that.menuId) && Objects.equals(getProduct(), that.getProduct()) && Objects.equals(getQuantity(), that.getQuantity());
    }

    @Override
    public int hashCode() {
        return Objects.hash(menuId, getProduct(), getQuantity());
    }

    public void addedBy(Long menuId) {
        this.menuId = menuId;
    }
}
