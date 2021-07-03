package kitchenpos.domain.menuproduct;

import kitchenpos.domain.Price;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.product.Product;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    private Menu menu;

    @ManyToOne
    private Product product;

    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, Quantity quantity) {
        this(null, product, quantity);
    }

    public MenuProduct(Menu menu, Product product, Quantity quantity) {
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    void changeMenu(Menu menu) {
        if (this.menu != null) {
            throw new IllegalStateException("이미 메뉴가 등록되어 있으면, 불가능합니다.");
        }

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

    public Price getAmount() {
        return product.multiplyPrice(quantity);
    }

    public Quantity getQuantity() {
        return quantity;
    }
}
