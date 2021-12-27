package kitchenpos.menu.domain.domain;

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
import kitchenpos.product.domain.domain.Product;
import kitchenpos.product.exception.NotFoundProductException;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long id, Product product, long quantity) {
        validate(product);
        this.id = id;
        this.product = product;
        this.quantity = Quantity.of(quantity);
    }

    private void validate(Product product) {
        if (Objects.isNull(product)) {
            throw new NotFoundProductException();
        }
    }

    public static MenuProduct of(Product product, long quantity) {
        return of(null, product, quantity);
    }

    public static MenuProduct of(Long id, Product product, long quantity) {
        return new MenuProduct(id, product, quantity);
    }

    public Price getTotalPrice() {
        if (null == product) {
            throw new NotFoundProductException();
        }
        return product.getPrice().multiply(quantity.getQuantity());
    }

    public Long getId() {
        return id;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }
}
