package kitchenpos.table.domain;

public class OrderTable {
    public static final String TABLE_GROUP_EMPTY_EXCEPTION_MESSAGE = "테이블 그룹이 존재하지 않습니다.";
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
        this.empty = false;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void empty() {
        if (this.tableGroupId == null) {
            throw new IllegalArgumentException(TABLE_GROUP_EMPTY_EXCEPTION_MESSAGE);
        }
        this.empty = true;
    }
}
