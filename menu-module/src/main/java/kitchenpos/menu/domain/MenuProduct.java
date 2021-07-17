package kitchenpos.menu.domain;

import kitchenpos.product.domain.Product;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long seq;

    @JoinColumn(name = "product_id")
    @ManyToOne
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Product product, Long quantity) {
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public Price calculateMenuProductPrice() {
        return product.getPrice()
                .multiply(new Price(BigDecimal.valueOf(quantity.value())));
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
}
