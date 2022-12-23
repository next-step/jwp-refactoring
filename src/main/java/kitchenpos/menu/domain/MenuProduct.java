package kitchenpos.menu.domain;


import javax.persistence.*;
import java.math.BigDecimal;

@Entity
public class MenuProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "menu_id")
    private Menu menu;

    @Column(nullable = false)
    private Long productId;
    @Column(nullable = false)
    private Long quantity;

    protected MenuProduct() {
    }

    public MenuProduct(Long productId, Long quantity) {
        this.quantity = quantity;
        this.productId = productId;
    }

    public MenuProduct(Long seq, Long productId, Long quantity) {
        this.seq = seq;
        this.productId = productId;
        this.quantity = quantity;
    }

    public void setMenu(Menu menu) {
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

    public long getQuantity() {
        return quantity;
    }
}


