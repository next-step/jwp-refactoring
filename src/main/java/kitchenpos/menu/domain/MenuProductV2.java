package kitchenpos.menu.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class MenuProductV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private Long menuId;

    @Column(nullable = false)
    private Long productId;

    @Column(nullable = false)
    private Long quantity;

    protected MenuProductV2() {
    }

    public MenuProductV2(Long seq, Long menuId, Long productId, Long quantity) {
        this.seq = seq;
        this.menuId = menuId;
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
        return Objects.equals(seq, that.seq) && Objects.equals(menuId, that.menuId)
                && Objects.equals(productId, that.productId) && Objects.equals(quantity, that.quantity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seq, menuId, productId, quantity);
    }
}
