package kitchenpos.menu.domain;

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

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        validate(menu, product, quantity);

        updateMenu(menu);
        this.productId = productId;
        this.quantity = quantity;
    }

    private void validate(Menu menu, Product product, long quantity) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException("메뉴 상품에는 메뉴가 필수값 입니다.");
        }

        if (Objects.isNull(product)) {
            throw new IllegalArgumentException("메뉴 상품에는 상품이 필수값 입니다.");
        }

        if (quantity < 0) {
            throw new IllegalArgumentException("수량은 1개 이상이여야 합니다.");
        }
    }

    void updateMenu(Menu menu) {
        if (this.menu != menu) {
            this.menu = menu;
            menu.addMenuProduct(this);
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }
}
