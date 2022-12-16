package kitchenpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Objects;

@Entity
@Table(name = "menu_product")
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @Column
    private long quantity;

    private MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(product, quantity);
    }

    public MenuProduct() {
    }

    public Long getSeq() {
        return seq;
    }

    @JsonIgnore // infinite recursion을 피하기 위함
    public Menu getMenu() {
        return menu;
    }

    public void updateMenu(final Menu menu) {
        this.menu = menu;
    }

    public Product getProduct() {
        return product;
    }

    public BigDecimal totalProductPrice() {
        return this.product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    public long getQuantity() {
        return quantity;
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
        return quantity == that.quantity && Objects.equals(menu, that.menu) && Objects.equals(product,
                that.product);
    }

    @Override
    public int hashCode() {
        return Objects.hash(menu, product, quantity);
    }
}
