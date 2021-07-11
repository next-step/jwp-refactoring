package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    @ManyToOne(fetch = FetchType.LAZY)
    private Menu menu;

    @Column(name = "product_id")
    private Long productId;

    private Long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long productId, Long quantity) {
        this(null, null, productId, quantity);
    }

    public MenuProduct(Long seq, Menu menu, Long productId, Long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

//    public BigDecimal sumMenuProduct() {
//        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
//    }

    public void registerMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public Long getProductId() {
        return productId;
    }

    public Long getQuantity() {
        return quantity;
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private MenuProduct menuProduct = new MenuProduct();

        public Builder id(Long seq) {
            menuProduct.seq = seq;
            return this;
        }

        public Builder menu(Menu menu) {
            menuProduct.menu = menu;
            return this;
        }

        public Builder productId(Long productId) {
            menuProduct.productId = productId;
            return this;
        }

        public Builder quantity(Long quantity) {
            menuProduct.quantity = quantity;
            return this;
        }

        public MenuProduct build() {
            return menuProduct;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof MenuProduct)) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(getSeq(), that.getSeq());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSeq());
    }

    @Override
    public String toString() {
        return "MenuProduct{" +
                "seq=" + seq +
                ", menu=" + menu +
                ", productId=" + productId +
                ", quantity=" + quantity +
                '}';
    }
}
