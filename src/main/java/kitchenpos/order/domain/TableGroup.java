package kitchenpos.order.domain;

import kitchenpos.order.exceptions.InputTableDataErrorCode;
import kitchenpos.order.exceptions.InputTableDataException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TableGroup {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTable> orderTables;

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this(createdDate, orderTables);
        this.id = id;
    }

    public TableGroup(Long id, LocalDateTime createdDateTime) {
        this.id = id;
        this.createdDate = createdDateTime;
        this.orderTables = new ArrayList<>();
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        validateEnrolledTable(orderTables);
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void createDate() {
        this.createdDate = LocalDateTime.now();
    }

    public void receiveOrderTables(List<OrderTable> savedOrderTables) {
        this.orderTables = savedOrderTables;
    }

    public void cancleGroup() {
        this.orderTables.stream()
                .forEach(it -> it.emptyTableGroupId());
        this.orderTables = Collections.emptyList();
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
