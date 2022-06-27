package kitchenpos.domain;

import static javax.persistence.GenerationType.IDENTITY;

import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;
    private Long tableGroupId;
    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.tableGroupId = null;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void changeIsEmpty(boolean isEmpty) {
        if (Objects.isNull(isEmpty) || Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
        this.empty = isEmpty;
    }

    public void unGroupTable() {
        this.empty = true;
        this.numberOfGuests.changeNumberOfGuests(0);
        this.tableGroupId = null;
    }

    public void mapToTableGroup(long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests.changeNumberOfGuests(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getGuests();
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof OrderTable)) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(
            id, that.id) && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
