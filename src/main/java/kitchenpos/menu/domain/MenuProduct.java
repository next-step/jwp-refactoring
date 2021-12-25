package kitchenpos.menu.domain;

import java.math.BigDecimal;
import java.util.Objects;

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

import kitchenpos.common.Price;
import kitchenpos.common.Quantity;
import kitchenpos.menu.exception.NotFoundMenuException;
import kitchenpos.product.domain.Product;
import kitchenpos.product.exception.NotFoundProductException;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

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

    protected MenuProduct() {
    }

    private MenuProduct(Long seq, Menu menu, Product product, long quantity) {
        validate(product);
        this.seq = seq;
        this.menu = menu;
        this.product = product;
        this.quantity = Quantity.of(quantity);
    }

    private void validate(Product product) {
        if (Objects.isNull(product)) {
            throw new NotFoundProductException();
        }
    }

    public static MenuProduct of(Product product, long quantity) {
        return of(null, null, product, quantity);
    }

    public static MenuProduct of(Long seq, Menu menu, Product product, long quantity) {
        return new MenuProduct(seq, menu, product, quantity);
    }

    public Price getTotalPrice() {
        if (null == product) {
            throw new NotFoundProductException();
        }
        return product.getPrice().multiply(quantity.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }

    public void setMenu(Menu menu) {
        if (Objects.isNull(menu)) {
            throw new NotFoundMenuException();
        }
        this.menu = menu;
    }
}
