package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class MenuProduct {
    
    private static int MIN_MENU_PRODUCT_QUANTITY = 1;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Menu menu, Product product, long quantity) {
        validate(menu, product, quantity);

        updateMenu(menu);
        this.product = product;
        this.quantity = quantity;
    }

    private void validate(Menu menu, Product product, long quantity) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException("메뉴 상품에는 메뉴가 필수값 입니다.");
        }

        if (Objects.isNull(product)) {
            throw new IllegalArgumentException("메뉴 상품에는 상품이 필수값 입니다.");
        }

        if (quantity < MIN_MENU_PRODUCT_QUANTITY) {
            throw new IllegalArgumentException("메뉴 상품의 상품 수량은 1개 이상이여야 합니다.");
        }
    }

    void updateMenu(Menu menu) {
        if (this.menu != menu) {
            this.menu = menu;
            menu.addMenuProduct(this);
        }
    }

    public Price getTotalPrice() {
        return this.product.getPrice().multiply(quantity);
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
