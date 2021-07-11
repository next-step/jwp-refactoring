package kitchenpos.menu.domain;

import kitchenpos.common.domain.Quantity;

import javax.persistence.*;

@Entity
public class MenuProduct {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "product_id")
    private Long productId;

    @Embedded
    private Quantity quantity;

    public MenuProduct() {
    }

    public MenuProduct(Long productId, Quantity quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Menu menu, Long productId, Quantity quantity) {
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
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

    public void matchMenu(Menu menu) {
        this.menu = menu;
    }
}
