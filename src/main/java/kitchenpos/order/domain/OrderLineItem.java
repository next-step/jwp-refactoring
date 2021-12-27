package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.common.vo.Quantity;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@Entity
public class OrderLineItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seq")
    private Long id;

    @JoinColumn(name = "order_id", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order;

    private Long menuId;

    @Embedded
    @Column(nullable = false)
    private Quantity quantity;

    protected OrderLineItem() {
    }

    public OrderLineItem(Long menuId, Quantity quantity) {
        this.menuId = menuId;
        this.quantity = quantity;
    }

    public void assignOrder(Order order) {
        this.order = order;
    }

    public Long getId() {
        return id;
    }

    public Order getOrder() {
        return order;
    }

    public Long getMenuId() {
        return menuId;
    }

    public Quantity getQuantity() {
        return quantity;
    }

    public long getQuantityVal() {
        return quantity.getQuantity();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof OrderLineItem)) {
            return false;
        }

        OrderLineItem that = (OrderLineItem) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
