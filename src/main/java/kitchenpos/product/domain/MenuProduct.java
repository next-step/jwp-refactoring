package kitchenpos.product.domain;

import kitchenpos.common.domain.Quantity;
import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    private Long menuId;

    @ManyToOne(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "product_id")
    private Product product;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {

    }

    public MenuProduct(Long menuId, Product product, long quantity) {
        this.menuId = menuId;
        this.product = product;
        this.quantity = new Quantity(quantity);
    }

    public BigDecimal getMenuProductPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity.getQuantity()));
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }
}
