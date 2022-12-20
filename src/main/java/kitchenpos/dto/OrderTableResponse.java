package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.Objects;

public class OrderTableResponse {
    private Long id;
    private Long tableGroup;
    private int numberOfGuests;
    private boolean empty;

    private OrderTableResponse(Long id, TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        this.id = id;
        this.tableGroup = tableGroupNullCheck(tableGroup);
        this.numberOfGuests = numberOfGuests;
        this.empty = isEmpty;
    }

    public static OrderTableResponse from(OrderTable orderTable) {
        return new OrderTableResponse(
                orderTable.getId(),
                orderTable.getTableGroup(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty());
    }

    private Long tableGroupNullCheck(TableGroup tableGroup) {
        if (Objects.isNull(tableGroup)) {
            return null;
        }

        return tableGroup.getId();
    }


    public Long getId() {
        return id;
    }

    public Long getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
