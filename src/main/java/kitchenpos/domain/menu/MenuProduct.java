package kitchenpos.domain.menu;

import java.math.BigDecimal;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.domain.Quantity;
import kitchenpos.domain.product.Product;

@Entity
@Table(name = "menu_product")
public class MenuProduct {
    private static final String NOT_EXIST_MENU = "Menu 가 존재하지 않습니다.";
    private static final String NOT_EXIST_PRODUCT = "Product 가 존재하지 않습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
    private Menu menu;

    @ManyToOne
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {}

    private MenuProduct(Long seq, Menu menu, Product product, long quantity) {
        this(product, quantity);
        this.seq = seq;
        this.menu = menu;
    }

    private MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = Quantity.from(quantity);
    }

    public static MenuProduct of(Long seq, Menu menu, Product product, long quantity) {
        validateExistMenu(menu);
        validateProduct(product);

        return new MenuProduct(seq, menu, product, quantity);
    }

    public static MenuProduct of(Product product, long quantity) {
        validateProduct(product);
        return new MenuProduct(product, quantity);
    }

    public void assignMenu(Menu menu) {
        this.menu = menu;
    }

    public BigDecimal calculateTotalPrice() {
        BigDecimal productQuantity = BigDecimal.valueOf(this.quantity.getValue());
        return product.getPrice().getValue().multiply(productQuantity);
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

    public Quantity getQuantity() {
        return quantity;
    }

    private static void validateExistMenu(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new IllegalArgumentException(NOT_EXIST_MENU);
        }
    }

    private static void validateProduct(Product product) {
        if (Objects.isNull(product)) {
            throw new IllegalArgumentException(NOT_EXIST_PRODUCT);
        }
    }
}
