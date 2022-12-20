package kitchenpos.table.domain;

import kitchenpos.table.message.OrderTableMessage;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @Column
    private Long tableGroupId;

    protected OrderTable() {

    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(Integer numberOfGuests, boolean empty) {
        return new OrderTable(NumberOfGuests.of(numberOfGuests), empty);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNotEmpty() {
        return !this.empty;
    }

    public void changeEmpty(boolean empty) {
        if(isGrouped()) {
            throw new IllegalArgumentException(OrderTableMessage.CHANGE_EMPTY_ERROR_TABLE_GROUP_MUST_BE_NOT_ENROLLED.message());
        }
        this.empty = empty;
    }

    public boolean isGrouped() {
        return this.tableGroupId != null;
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        if(isEmpty()) {
            throw new IllegalArgumentException(OrderTableMessage.CHANGE_GUESTS_ERROR_TABLE_MUST_BE_NOT_EMPTY_STATE.message());
        }
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public void groupBy(Long tableGroupId) {
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderTable that = (OrderTable) o;

        if (empty != that.empty) return false;
        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(numberOfGuests, that.numberOfGuests);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (numberOfGuests != null ? numberOfGuests.hashCode() : 0);
        result = 31 * result + (empty ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                ", tableGroupId=" + tableGroupId +
                '}';
    }
}
