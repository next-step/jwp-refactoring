package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;
import org.springframework.util.Assert;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long seq, Menu menu, Product product, Quantity quantity) {
        Assert.notNull(product, "상품은 비어있을 수 없습니다.");
        Assert.notNull(quantity, "수량은 비어있을 수 없습니다.");

        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = quantity;
    }

    public static MenuProduct of(Product product, Quantity quantity) {
        return new MenuProduct(null, null, product, quantity);
    }

    public void updateMenu(Menu menu) {
        this.menu = menu;
    }

    public BigDecimal getOriginalPrice() {
        BigDecimal productPrice = product.getPrice();

        return productPrice.multiply(BigDecimal.valueOf(quantity.getQuantity()));
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getProductId() {
        return product.getId();
    }

    public Long getQuantity() {
        return quantity.getQuantity();
    }
}
