package kitchenpos.order.domain;

import kitchenpos.BaseEntity;
import kitchenpos.order.dto.OrderTableResponse;

import javax.persistence.*;
import java.util.List;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private final OrderTables orderTables = new OrderTables();

    public TableGroup() {
    }

    public void add(List<OrderTable> orderTables) {
        this.orderTables.add(this.id, orderTables);
    }

    public Long getId() {
        return id;
    }

    public List<OrderTableResponse> getOrderTableResponses() {
        return orderTables.getOrderTableResponses();
    }
}
