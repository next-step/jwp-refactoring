package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import kitchenpos.common.domain.Quantity;

@Entity
@Table(name = "menu_product")
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "product_id")
    private Long productId;

    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(final Long productId,
                       final Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(final Long seq,
                       final Menu menu,
                       final Long productId,
                       final Quantity quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void relateToMenu(final Menu menu) {
        if (Objects.nonNull(this.menu)) {
            throw new IllegalStateException("이미 메뉴와의 연관관계가 설정되어 있습니다.");
        }
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
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
}
