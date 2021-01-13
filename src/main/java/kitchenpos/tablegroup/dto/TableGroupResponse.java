package kitchenpos.tablegroup.dto;

import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

public class TableGroupResponse {

    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableResponse> orderTables;

    private TableGroupResponse(final Long id, final LocalDateTime createdDate, final List<OrderTableResponse> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public static TableGroupResponse of(final TableGroup tableGroup) {
        return new TableGroupResponse(tableGroup.getId(),
                tableGroup.getCreatedDate(),
                OrderTableResponse.ofList(tableGroup.getOrderTables()));
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTableResponse> getOrderTables() {
        return orderTables;
    }

    public static class OrderTableResponse {

        private Long id;
        private Long tableGroupId;
        private int numberOfGuests;
        private boolean empty;

        private OrderTableResponse(final Long id, final Long tableGroupId, final int numberOfGuests, final boolean empty) {
            this.id = id;
            this.tableGroupId = tableGroupId;
            this.numberOfGuests = numberOfGuests;
            this.empty = empty;
        }

        public static OrderTableResponse of(final OrderTable orderTable) {
            return new OrderTableResponse(orderTable.getId(),
                    orderTable.getTableGroup()
                            .getId(),
                    orderTable.getNumberOfGuests(),
                    orderTable.isEmpty());
        }

        public static List<OrderTableResponse> ofList(final List<OrderTable> orderTables) {
            return orderTables.stream()
                    .map(OrderTableResponse::of)
                    .collect(Collectors.toList());
        }

        public Long getId() {
            return id;
        }

        public Long getTableGroupId() {
            return tableGroupId;
        }

        public int getNumberOfGuests() {
            return numberOfGuests;
        }

        public boolean isEmpty() {
            return empty;
        }
    }
}
