package kitchenpos.table_group.domain;

import kitchenpos.table.domain.OrderTable;

public class GroupTable {
    private static final String NOT_EMPTY_OR_GROUPED_TABLE_CANNOT_GROUP = "빈 테이블이 아니거나 이미 단체 지정되었다면 단체 지정 할 수 없습니다";

    private Long id;
    private boolean grouped;
    private boolean empty;

    public GroupTable(Long id, boolean grouped, boolean empty) {
        this.id = id;
        this.grouped = grouped;
        this.empty = empty;
    }

    public GroupTable(OrderTable orderTable) {
        id = orderTable.getId();
        grouped = orderTable.getTableGroupId() != null;
        empty = orderTable.isEmpty();
    }

    public void group() {
        if (!isEmpty() || isGrouped()) {
            throw new IllegalArgumentException(NOT_EMPTY_OR_GROUPED_TABLE_CANNOT_GROUP);
        }
        grouped = true;
        empty = false;
    }

    public Long getId() {
        return id;
    }

    public boolean isGrouped() {
        return grouped;
    }

    public boolean isEmpty() {
        return empty;
    }
}
