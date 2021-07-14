package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;

import kitchenpos.generic.quantity.domain.Quantity;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Long productId;

    @Embedded
    @Column(nullable = false)
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public boolean isSatisfiedBy(MenuDetailOption menuDetailOption) {
        if (!this.quantity.equals(menuDetailOption.getQuantity())) {
            return false;
        }

        // return product.isSatisfiedBy(menuDetailOption); // TODO 어떻게 풀어갈것인가?
        return true;
    }
}
