package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.domain.Quantity;
import kitchenpos.exception.InvalidArgumentException;
import kitchenpos.common.domain.Price;
import kitchenpos.product.domain.Product;

@Entity
public class MenuProduct extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    private MenuProduct(Product product, Long quantity) {
        this.quantity = Quantity.valueOf(quantity);
        setProduct(product);
    }

    public static MenuProduct of(Product product, Long quantity) {
        return new MenuProduct(product, quantity);
    }

    public void setMenu(Menu menu) {
        validateMenu(menu);
        if (this.menu != null) {
            this.menu.removeMenuProduct(this);
        }
        this.menu = menu;
        menu.addMenuProduct(this);
    }

    public boolean equalMenu(Menu menu) {
        if (this.menu == null) {
            return null == menu;
        }
        return this.menu.equals(menu);
    }

    public boolean equalMenuProduct(MenuProduct other) {
        return equalMenu(other.menu) && product.equals(other.product) && quantity.equals(other.quantity);
    }

    public void removeMenu() {
        this.menu = null;
    }

    public Price getPrice(){
        return product.multiplyQuantity(quantity);
    }

    public Product getProduct() {
        return this.product;
    }

    private void setProduct(Product product) {
        validateProduct(product);
        this.product = product;
    }

    private void validateMenu(Menu menu) {
        if (menu == null) {
            throw new InvalidArgumentException("메뉴는 필수입니다.");
        }
    }

    private void validateProduct(Product product) {
        if (product == null) {
            throw new InvalidArgumentException("상품은 필수입니다.");
        }
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
        return seq.equals(that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }





    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getMenuId() {
        return menu.getId();
    }

    public void setMenuId(final Long menuId) {
//        this.menuId = menuId;
    }

    public Long getProductId() {
        return product.getId();
    }

    public void setProductId(final Long productId) {
//        this.productId = productId;
    }

    public long getQuantity() {
        return quantity.getQuantity();
    }

    public void setQuantity(final long quantity) {
//        this.quantity = quantity;
    }



}
