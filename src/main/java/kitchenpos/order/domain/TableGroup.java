package kitchenpos.order.domain;

import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime createdDate;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {

    }

    public TableGroup(OrderTables orderTables, LocalDateTime createdDate) {
        validateEnrolledTable(orderTables.getOrderTables());
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public void createDate() {
        this.createdDate = LocalDateTime.now();
    }

    public void receiveOrderTables(OrderTables savedOrderTables) {
        this.orderTables = savedOrderTables;
    }

    public void cancleGroup() {
        this.orderTables.cancleGroup();
    }

    private void validateEnrolledTable(List<OrderTable> orderTables) {
        if (hasEmptyTable(orderTables)) {
            throw new InputTableDataException(InputTableDataErrorCode.THE_TABLE_CAN_NOT_REGISTER_GROUP_BECAUSE_OF_EMPTY_STATUS);
        }
    }

    private boolean hasEmptyTable(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(it -> it.isEmpty());
    }
}
