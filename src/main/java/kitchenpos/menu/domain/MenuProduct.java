package kitchenpos.menu.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.product.domain.Product;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
public class MenuProduct extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;
    @ManyToOne
    private Product product;
    @Column
    private long quantity;

    public MenuProduct(Product product, long quantity) {
        validate(product, quantity);
        this.product = product;
        this.quantity = quantity;
    }

    protected MenuProduct() {
    }

    public BigDecimal getSumPriceOfProducts() {
        return product.getSumOfProducts(quantity);
    }

    public MenuProduct updateMenu(Menu menu) {
        this.menu = menu;
        return this;
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

    private void validate(Product product, long quantity) {
        if (product == null) {
            throw new IllegalArgumentException("메뉴의 상품은 지정되어야합니다.");
        }
        if (quantity < 0) {
            throw new IllegalArgumentException("메뉴의 수량은 음수가 될 수 없습니다. 0원 이상이어야합니다.");
        }
    }
}
