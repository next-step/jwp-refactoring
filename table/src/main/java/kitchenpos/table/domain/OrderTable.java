package kitchenpos.table.domain;

import java.util.Objects;

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
    private NumberOfGuests numberOfGuests;
    private boolean empty;

    protected OrderTable() {}

    private OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable generate(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public boolean isTableGroupRegistered() {
        return Objects.nonNull(tableGroupId);
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateEmpty();
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    private void validateEmpty() {
        if (empty) {
            throw new IllegalArgumentException("테이블이 비어있습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }
}
