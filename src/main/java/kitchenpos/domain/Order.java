package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import org.springframework.util.ObjectUtils;

@Entity
@Table(name = "OrderInfo")
public class Order extends BaseTimeEntity {

    @Id
    @Column(name = "ORDER_ID")
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long orderTableId;
    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;
    @OneToMany(mappedBy = "order", cascade = {CascadeType.ALL}, orphanRemoval = true)
    private List<OrderLineItem> orderLineItems = new LinkedList<>();

    public Order() {
    }

    public Long getId() {
        return id;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public List<OrderLineItem> getOrderLineItems() {
        return orderLineItems;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Order)) {
            return false;
        }
        Order order = (Order) o;
        return Objects.equals(id, order.id) && Objects.equals(orderTableId,
            order.orderTableId) && orderStatus == order.orderStatus && Objects.equals(
            orderLineItems, order.orderLineItems);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, orderTableId, orderStatus, orderLineItems);
    }
}
