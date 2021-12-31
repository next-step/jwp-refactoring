package kitchenpos.order.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;

import javax.persistence.*;
import java.util.List;

@Entity
public class TableGroup extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private OrderTables orderTables;

    protected TableGroup() {

    }

    public TableGroup(OrderTables orderTables) {
        validateTable(orderTables);
        this.orderTables = orderTables;
        allocateTableGroup();
    }

    private void allocateTableGroup() {
        this.orderTables.getOrderTables().stream()
                .forEach(orderTable -> orderTable.allocateTableGroup(this.getId()));
    }

    public Long getId() {
        return id;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables.getOrderTables();
    }

    public void cancleGroup(List<Order> orders, OrderTableValidator orderTableValidator) {
        orderTableValidator.cancelTableGroup(orders);
        this.orderTables.cancleGroup();
    }

    public void cancleGroup() {
        this.orderTables.cancleGroup();
    }

    private void validateTable(OrderTables orderTables) {
        if (hasEmptyTable(orderTables.getOrderTables())) {
            throw new InputTableDataException(InputTableDataErrorCode.THE_TABLE_CAN_NOT_REGISTER_GROUP_BECAUSE_OF_EMPTY_STATUS);
        }
    }

    private boolean hasEmptyTable(List<OrderTable> orderTables) {
        return orderTables.stream()
                .anyMatch(orderTable -> orderTable.isEmpty());
    }
}
