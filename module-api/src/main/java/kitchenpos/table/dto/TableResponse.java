package kitchenpos.table.dto;

import kitchenpos.table.domain.OrderTable;

import java.util.Objects;

public class TableResponse {
    private Long id;
    private Long tableGroupId;
    private Integer numberOfGuests;
    private Boolean empty;

    protected TableResponse() {
    }

    public TableResponse(
            final Long id,
            final Long tableGroupId,
            final Integer numberOfGuests,
            final Boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static TableResponse of(final OrderTable orderTable) {
        Long tableGroupId = null;
        if (orderTable.getTableGroup() != null) {
            tableGroupId = orderTable.getTableGroup().getId();
        }

        return new TableResponse(
                orderTable.getId(),
                tableGroupId,
                orderTable.getNumberOfGuests(),
                orderTable.getEmpty()
        );
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests;
    }

    public Boolean getEmpty() {
        return empty;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableResponse that = (TableResponse) o;
        return Objects.equals(id, that.id) &&
                Objects.equals(tableGroupId, that.tableGroupId) &&
                Objects.equals(numberOfGuests, that.numberOfGuests) &&
                Objects.equals(empty, that.empty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
