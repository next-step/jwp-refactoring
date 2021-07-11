package kitchenpos.domain;

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

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    @Embedded
    @Column(nullable = false)
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, Quantity quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Price getTotalPrice() {
        return product.priceOf(quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public Product getProduct() {
        return product;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public boolean isSatisfiedBy(MenuDetailOption menuDetailOption) {
        if (!this.quantity.equals(menuDetailOption.getQuantity())) {
            return false;
        }

        return product.isSatisfiedBy(menuDetailOption);
    }
}
