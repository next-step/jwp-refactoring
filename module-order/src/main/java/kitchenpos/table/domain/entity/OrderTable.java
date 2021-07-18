package kitchenpos.table.domain.entity;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.table.domain.value.NumberOfGuests;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, NumberOfGuests numberOfGuests,
        boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    public OrderTable(Long id) {
        this.id = id;
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

    public void toTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    @Override
    public String toString() {
        return "OrderTable{" +
            "id=" + id +
            ", tableGroup=" + tableGroupId +
            ", numberOfGuests=" + numberOfGuests +
            ", empty=" + empty +
            '}';
    }
}
