package kitchenpos.table.dto;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;

public class TableGroupRequest {

    private List<OrderTableDto> orderTables;

    private TableGroupRequest() {}

    public TableGroupRequest(List<OrderTableDto> orderTables) {
        this.orderTables = orderTables;
    }

    public List<Long> getOrderTableIds() {
        return orderTables.stream()
                .map(OrderTableDto::getId)
                .collect(Collectors.toList());
    }

    public List<OrderTableDto> getOrderTables() {
        return orderTables;
    }

    public TableGroup toTableGroup(List<OrderTable> orderTables) {
        return new TableGroup.Builder()
                .orderTables(orderTables)
                .build();
    }

    public static class Builder {

        private List<OrderTableDto> orderTables;

        public Builder orderTables(List<OrderTableDto> orderTables) {
            this.orderTables = orderTables;
            return this;
        }

        public TableGroupRequest build() {
            return new TableGroupRequest(orderTables);
        }
    }


    public static class OrderTableDto {

        private Long id;

        public OrderTableDto() {}

        public OrderTableDto(Long id) {
            this.id = id;
        }

        public Long getId() {
            return id;
        }

        public static class Builder {

            private Long id;

            public Builder id(Long id) {
                this.id = id;
                return this;
            }

            public OrderTableDto build() {
                return new OrderTableDto(id);
            }
        }

    }

}
