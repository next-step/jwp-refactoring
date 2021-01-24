package kitchenpos.table.dto;

public class TableResponse {
    private long id;
    private Long tableGroupId;
    private int numberOfGuests;

    protected TableResponse(){}

    public TableResponse(long id, Long tableGroupId, int numberOfGuests) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
    }

    public long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }
}
