package kitchenpos.table_group.dto;

import kitchenpos.table_group.domain.GroupTable;

public class TableGroupOrderTableResponse {
    private Long id;
    private boolean empty;

    public TableGroupOrderTableResponse(GroupTable groupTable) {
        id = groupTable.getId();
        empty = groupTable.isEmpty();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }
}
