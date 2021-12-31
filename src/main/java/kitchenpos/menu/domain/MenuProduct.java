package kitchenpos.menu.domain;

import kitchenpos.global.BaseTimeEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "menu_product")
public class MenuProduct extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq", length = 20)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @JoinColumn(name = "product_id")
    private Long product;

    @Column(name = "quantity", length = 20, nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(final Long product, final long quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public void addMenu(Menu menu) {
        this.menu = menu;
    }

    public Long getSeq() {
        return seq;
    }

    public Menu getMenu() {
        return menu;
    }

    public Long getProduct() {
        return product;
    }

    public long getQuantity() {
        return quantity;
    }

}
