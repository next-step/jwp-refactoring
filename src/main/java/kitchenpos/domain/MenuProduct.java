package kitchenpos.domain;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.exception.AlreadyAllocatedException;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(nullable = false, foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private Product product;

    @Column(nullable = false)
    private long quantity;

    public MenuProduct() {
    }

    public MenuProduct(Product product, long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void setMenu(Menu menu) {
        checkAllocation();
        this.menu = menu;
    }

    private void checkAllocation() {
        if (this.menu != null) {
            throw new AlreadyAllocatedException("이미 할당 된 메뉴-제품 입니다.");
        }
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Product getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

    public BigDecimal getTotalPrice() {
        return product.getPrice().multiply(BigDecimal.valueOf(quantity));
    }

    // TODO 삭제 대상

    public Long getProductId() {
        return product.getId();
    }

    public MenuProduct(Long seq, Long menuId, Long productId, long quantity) {
        this.seq = seq;
        this.menu.setId(menuId);
        this.product.setId(productId);
        this.quantity = quantity;
    }

    public MenuProduct(Long productId, Long menuId) {
        setMenuId(menuId);
        setProductId(productId);
    }

    public void setSeq(Long seq) {
        this.seq = seq;
    }

    public void setMenuId(Long menuId) {
        this.menu.setId(menuId);;
    }

    public void setProductId(Long productId) {
        this.product.setId(productId);
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    public Long getMenuId() {
        return menu.getId();
    }
}
