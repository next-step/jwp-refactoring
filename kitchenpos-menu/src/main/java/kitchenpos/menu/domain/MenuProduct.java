package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.vo.Quantity;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @JoinColumn(name = "menu_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    private Long productId;

    @Column(nullable = false)
    private Quantity quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, Quantity quantity) {
        this(null, null, productId, quantity);
    }

    public MenuProduct(Menu menu, Long productId, Quantity quantity) {
        this(null, menu, productId, quantity);
    }

    public MenuProduct(Long id, Menu menu, Long productId, Quantity quantity) {
        this.id = id;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void assignMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getId() {
        return id;
    }

    public Menu getMenu() {
        return menu;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public long getQuantityVal() {
        return quantity.getQuantity();
    }

    public Long getProductId() {
        return productId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof MenuProduct)) {
            return false;
        }
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
