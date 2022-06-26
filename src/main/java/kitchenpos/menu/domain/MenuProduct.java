package kitchenpos.menu.domain;

import static java.util.Objects.requireNonNull;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
    @Column(name = "product_id", nullable = false)
    private Long productId;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity) {
        requireNonNull(productId, "상품이 존재하지 않습니다.");

        this.productId = productId;
        this.quantity = new Quantity(quantity);
    }

    public Long getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
            "id=" + id +
            ", menu=" + menu +
            ", productId=" + productId +
            ", quantity=" + quantity +
            '}';
    }

}
