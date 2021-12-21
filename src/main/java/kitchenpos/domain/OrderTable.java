package kitchenpos.domain;

public class OrderTable {
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(final Long id) {
        this.id = id;
    }

    public OrderTable(boolean empty) {
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable(final Long id, final Long tableGroupId) {
        this.id = id;
        this.tableGroupId = tableGroupId;
    }

    public OrderTable(final Long id, final boolean empty) {
        this.id = id;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(numberOfGuests);
        this.empty = empty;
    }


    public OrderTable(final Long id, final Long tableGroupId, final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.empty = empty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.tableGroupId = tableGroupId;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this(tableGroupId, numberOfGuests, empty);
        this.id = id;
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
}
