package kitchenpos.menu.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.domain.Quantity;
import org.springframework.util.Assert;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @JoinColumn(name = "menu_id", nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_menu_product_menu"))
    @ManyToOne(optional = false)
    private Menu menu;

    @Column(nullable = false, updatable = false)
    @JoinColumn(nullable = false, updatable = false, foreignKey = @ForeignKey(name = "fk_menu_product_product"))
    private long productId;

    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    private MenuProduct(long productId, Quantity quantity) {
        Assert.notNull(quantity, "수량은 필수입니다.");
        this.productId = productId;
        this.quantity = quantity;
    }

    public static MenuProduct of(long productId, Quantity quantity) {
        return new MenuProduct(productId, quantity);
    }

    public Long getSeq() {
        return seq;
    }

    public void setSeq(final Long seq) {
        this.seq = seq;
    }

    public Long getProductId() {
        return productId;
    }

    public void setProductId(final Long productId) {
        this.productId = productId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public void setQuantity(Quantity quantity) {
        this.quantity = quantity;
    }
}
