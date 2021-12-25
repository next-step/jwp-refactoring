package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        validateQuantity(quantity);
        this.product = product;
        this.quantity = quantity;
    }

    private void validateQuantity(long quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("메뉴 상품의 수량는 0개 이상 이어야 합니다.");
        }
    }

    public static MenuProduct of(Product product, long quantity) {
        return new MenuProduct(product, quantity);
    }

    public void changeMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
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

    public BigDecimal calculateAllPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }
}
