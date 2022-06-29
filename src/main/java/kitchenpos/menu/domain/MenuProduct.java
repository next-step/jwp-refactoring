package kitchenpos.menu.domain;

import kitchenpos.generic.Quantity;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false)
    private Menu menu;

    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long productId, final Long quantity) {
        this.productId = productId;
        this.quantity = new Quantity(quantity);
    }

    public MenuProduct(final Long productId, final long quantity) {
        this.productId = productId;
        this.quantity = new Quantity(quantity);
    }

    public void setMenu(final Menu menu) {
        this.menu = menu;
    }

    public Long getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getQuantity() {
        return quantity.getValue();
    }

    public Long getProductId() {
        return productId;
    }
}
