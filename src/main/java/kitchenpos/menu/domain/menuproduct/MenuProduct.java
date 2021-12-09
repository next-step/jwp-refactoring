package kitchenpos.menu.domain.menuproduct;

import kitchenpos.menu.domain.menu.Menu;
import kitchenpos.menu.domain.product.Product;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @ManyToOne(cascade = CascadeType.PERSIST, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        validation(menu, product, quantity);
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    private void validation(Menu menu, Product product, long quantity) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException("메뉴가 존재하지 않습니다.");
        }
        if (Objects.isNull(product)) {
            throw new IllegalArgumentException("상품이 존재하지 않습니다.");
        }
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
}
