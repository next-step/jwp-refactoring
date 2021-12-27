package kitchenpos.ordertable.dto;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.ordertable.domain.OrderTable;

public class OrderTableResponse {

    private Long id;

    private int numberOfGuests;

    private boolean orderClose;

    private Long tableGroupId;

    private OrderTableResponse() {
    }

    public OrderTableResponse(Long id, int numberOfGuests, boolean orderClose,
        Long tableGroupId) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.orderClose = orderClose;
        this.tableGroupId = tableGroupId;
    }

    public static List<OrderTableResponse> fromList(List<OrderTable> orderTables) {
        return orderTables.stream()
            .map(OrderTableResponse::from)
            .collect(Collectors.toList());
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(orderTable.getId(), orderTable.getNumberOfGuestsVal(),
            orderTable.isOrderClose(), orderTable.getTableGroupId());
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isOrderClose() {
        return orderClose;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTableResponse that = (OrderTableResponse) o;
        return getNumberOfGuests() == that.getNumberOfGuests()
            && isOrderClose() == that.isOrderClose()
            && Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getNumberOfGuests(), isOrderClose());
    }
}
