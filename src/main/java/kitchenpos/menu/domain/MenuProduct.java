package kitchenpos.menu.domain;

import java.util.Objects;
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
    private Long seq;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;
    private Long productId;
    @Embedded
    private MenuProductQuantity quantity;

    public MenuProduct() {}

    public MenuProduct(Long productId, MenuProductQuantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long seq, Menu menu, Long productId, MenuProductQuantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long seq, Menu menu, Long productId, int quantity) {
        this(seq, menu, productId, new MenuProductQuantity(quantity));
    }

    public void addMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public void setMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public MenuProductQuantity getQuantity() {
        return quantity;
    }

    public long getQuantityValue() {
        return quantity.getQuantity();
    }

    public void setQuantity(MenuProductQuantity quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq)
                && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, productId, quantity);
    }
}
