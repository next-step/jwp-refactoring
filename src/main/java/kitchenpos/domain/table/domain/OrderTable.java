package kitchenpos.domain.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public void changeEmpty(boolean empty) {
        validateTableGroup();
        this.empty = empty;
    }

    public void validateTableGroup() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateEmptyTable();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void checkNonEmptyInGroup() {
        if (!isEmpty() || hasTableGroup()) {
            throw new IllegalArgumentException();
        }
    }

    public void validateEmptyTable() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumber();
    }

    public boolean isEmpty() {
        return empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroupId);
    }
}
