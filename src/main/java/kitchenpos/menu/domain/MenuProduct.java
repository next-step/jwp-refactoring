package kitchenpos.menu.domain;

import static java.util.Objects.requireNonNull;

import java.math.BigDecimal;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", nullable = false)
    private Menu menu;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Embedded
    private Quantity quantity;

    // entity 기본생성자 이므로 사용 금지
    protected MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        requireNonNull(product, "상품이 존재하지 않습니다.");

        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public BigDecimal totalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity.getQuantity()));
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
        return product.getId();
    }

    public Product getProduct() {
        return product;
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
            ", menuId=" + menu.getId() +
            ", productId=" + product.getId() +
            ", quantity=" + quantity +
            '}';
    }

}
