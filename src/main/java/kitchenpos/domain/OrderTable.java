package kitchenpos.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    public OrderTable() {
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = Empty.of(empty);
    }

    public OrderTable(final Long id, final Long tableGroupId, final int numberOfGuests,
        final boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = Empty.of(empty);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public void setEmpty(final boolean empty) {
        this.empty = Empty.of(empty);
    }
}
