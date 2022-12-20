package kitchenpos.dto;

import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

import java.util.Objects;

public class TableResponse {
    private Long id;
    private Long tableGroup;
    private int numberOfGuests;
    private boolean empty;

    private TableResponse(Long id, TableGroup tableGroup, int numberOfGuests, boolean isEmpty) {
        this.id = id;
        this.tableGroup = tableGroupNullCheck(tableGroup);
        this.numberOfGuests = numberOfGuests;
        this.empty = isEmpty;
    }

    public static TableResponse from(OrderTable orderTable) {
        return new TableResponse(
                orderTable.getId(),
                orderTable.getTableGroup(),
                orderTable.getNumberOfGuests(),
                orderTable.isEmpty()
        );
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
