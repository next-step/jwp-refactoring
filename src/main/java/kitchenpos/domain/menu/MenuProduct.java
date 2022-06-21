package kitchenpos.domain.menu;

import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.domain.Quantity;
import kitchenpos.domain.product.Product;
import kitchenpos.exception.CreateMenuProductException;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

    private static final String PRODUCT_IS_NOT_NULL = "메뉴상품 생성 시 상품은 필수입니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
    private Menu menu;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
    private Product product;
    @Embedded
    private Quantity quantity;

    protected MenuProduct() {}

    private MenuProduct(Long seq, Menu menu, Product product, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = Quantity.from(quantity);
    }

    private MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = Quantity.from(quantity);
    }

    public static MenuProduct of(Product product, long quantity) {
        validateMenuProduct(product);
        return new MenuProduct(product, quantity);
    }

    public static MenuProduct of(Long seq, Menu menu, Product product, long quantity) {
        validateMenuProduct(product);
        return new MenuProduct(seq, menu, product, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return this.menu.getId();
    }

    public Long getProductId() {
        return this.product.getId();
    }

    public long findQuantity() {
        return quantity.getValue();
    }

    public void mappedByMenu(Menu menu) {
        this.menu = menu;
    }

    public BigDecimal calculateTotalPrice() {
        return product.calculatePrice(BigDecimal.valueOf(this.findQuantity()));
    }

    private static void validateMenuProduct(Product product) {
        if (product == null) {
            throw new CreateMenuProductException(PRODUCT_IS_NOT_NULL);
        }
    }
}
