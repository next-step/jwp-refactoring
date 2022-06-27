package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @Column(nullable = false)
    private Quantity quantity;
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    protected MenuProduct() {
    }

    public MenuProduct(Quantity quantity, Product product) {
        this.quantity = quantity;
        this.product = product;
    }

    public Price getTotalPrice() {
        Price price = product.getPrice();
        return new Price(price.getPrice() * quantity.getQuantity());
    }

    public Long getSeq() {
        return seq;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public Product getProduct() {
        return product;
    }

}
