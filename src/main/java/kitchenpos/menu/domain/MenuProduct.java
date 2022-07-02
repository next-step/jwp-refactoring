package kitchenpos.menu.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "menu_product")
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(name = "product_id", nullable = false)
    private Long productId;

    @Column(nullable = false)
    private long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, long quantity) {
        this.productId = productId;
        this.quantity = quantity;
    }

    public MenuProduct(Long seq, Menu menu, Long productId, long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getSeq() {
        return seq;
    }

    public Long getProductId() {
        return productId;
    }

    public long getQuantity() {
        return quantity;
    }

    public void connectedBy(Menu menu) {
        this.menu = menu;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MenuProduct that = (MenuProduct) o;
        return Objects.equals(seq, that.seq);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq);
    }
}
