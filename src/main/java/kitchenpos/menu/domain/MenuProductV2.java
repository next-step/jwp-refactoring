package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "menu_product")
public class MenuProductV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @ManyToOne
    @JoinColumn(name = "menu_id", nullable = false)
    private MenuV2 menu;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long quantity;

    protected MenuProductV2() {
    }

    public MenuProductV2(Long seq, MenuV2 menu, Long productId, Long quantity) {
        this.seq = seq;
        this.menu = menu;
        this.productId = productId;
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        MenuProductV2 that = (MenuProductV2) o;
        return Objects.equals(seq, that.seq) && Objects.equals(menu, that.menu)
                && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menu, productId, quantity);
    }
}
