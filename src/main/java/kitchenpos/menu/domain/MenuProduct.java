package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private Long productId;

    private Quantity quantity;

    protected MenuProduct() {
    }

    public Long getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProductId() {
        return productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    private MenuProduct(Long productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    private MenuProduct(Long id, Menu menu, Long productId, Quantity quantity) {
        this(productId, quantity);
        this.id = id;
        this.menu = menu;
    }

    public static MenuProduct of(Long productId, Quantity quantity) {
        return new MenuProduct(productId, quantity);
    }

    public static MenuProduct of(Long id, Menu menu, Long productId, Quantity quantity) {
        return new MenuProduct(id, menu, productId, quantity);
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }
}
