package kitchenpos.domain;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name = "orders") // TODO 변경하고 삭제
public class Order2 {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private OrderTable orderTable;

    @Enumerated(EnumType.STRING)
    private OrderStatus orderStatus;

    private LocalDateTime orderedTime = LocalDateTime.now();

    @OneToMany(mappedBy = "order")
    private List<OrderLineItem2> orderLineItems;

    protected Order2() {
    }

    public Order2(Long id, OrderTable orderTable, OrderStatus orderStatus, // TODO 추후 재 검토
        List<OrderLineItem2> orderLineItems) {
        this.id = id;
        this.orderTable = orderTable;
        this.orderStatus = orderStatus;
        this.orderLineItems = orderLineItems;
    }

    public boolean isCompleted() {
        return orderStatus.equals(OrderStatus.COMPLETION);
    }
}
