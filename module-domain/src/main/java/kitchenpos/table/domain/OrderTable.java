package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.common.exception.TableGroupAlreadyExistsException;

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

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return this.tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean hasTableGroup() {
        return !Objects.isNull(this.tableGroupId);
    }

    public void changeEmpty(boolean empty) {
        validateHasTabledGroup();
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests.changeNumberOfGuests(numberOfGuests);
    }

    private void validateHasTabledGroup() {
        if (hasTableGroup()) {
            throw new TableGroupAlreadyExistsException();
        }
    }

    public void groupBy(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }
}
