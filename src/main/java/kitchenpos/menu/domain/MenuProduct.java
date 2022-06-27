package kitchenpos.menu.domain;

import javax.persistence.*;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;
    @ManyToOne
    @JoinColumn(nullable = false)
    private Menu menu;
    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private long quantity;

    public MenuProduct(Menu menu, Long productId, long quantity) {
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    protected MenuProduct() {
    }

    public Long getSeq() {
        return seq;
    }

    public long getQuantity() {
        return quantity;
    }
}
