package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;
import org.springframework.util.Assert;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
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
    @JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    private Menu menu;

    @Column
    private Long productId;

    @Embedded
    private Quantity quantity;

    protected MenuProduct() {
    }

    private MenuProduct(Long seq, Menu menu, Long productId, Quantity quantity) {
        Assert.notNull(productId, "상품 ID는 비어있을 수 없습니다.");
        Assert.notNull(quantity, "수량은 비어있을 수 없습니다.");

        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(Long productId, Long quantity) {
        return new MenuProduct(null, null, productId, Quantity.of(quantity));
    }

    public void updateMenu(Menu menu) {
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
        return quantity.getQuantity();
    }
}
