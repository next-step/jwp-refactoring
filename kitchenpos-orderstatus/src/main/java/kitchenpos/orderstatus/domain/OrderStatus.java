package kitchenpos.orderstatus.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "order_status")
public class OrderStatus {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "order_id", nullable = false)
    private Long orderId;
    @Column(name = "order_table_id", nullable = false)
    private Long orderTableId;
    @Column(name = "status", nullable = false)
    @Enumerated(value = EnumType.STRING)
    private Status status;

    public OrderStatus(Long orderId, Long orderTableId, Status status) {
        this.orderId = orderId;
        this.orderTableId = orderTableId;
        this.status = status;
    }

    protected OrderStatus() {
    }

    public Long getId() {
        return id;
    }

    public Status getStatus() {
        return status;
    }

    public Long getOrderId() {
        return orderId;
    }

    public Long getOrderTableId() {
        return orderTableId;
    }

    public void updateStatus(Status changedStatus) {
        this.status = changedStatus;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderStatus that = (OrderStatus) o;
        return Objects.equals(orderId, that.orderId) && Objects.equals(orderTableId, that.orderTableId)
                && status == that.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(orderId, orderTableId, status);
    }
}
