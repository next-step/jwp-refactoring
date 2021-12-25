package kitchenpos.table.domain;

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

    public OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public OrderTable(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("비어 있는 테이블은 손님 수를 변경할 수 없습니다");
        }
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public void changeEmptyStatus(final boolean empty, OrderTableValidator validator) {
        validator.validateHasProgressOrder(this);
        this.empty = empty;
    }

    public void changeToNotEmpty() {
        this.empty = false;
    }

    public boolean hasTableGroup() {
        return tableGroupId != null;
    }

    public void setTableGroup(Long tableGroupId) {
        if (!isEmpty()) {
            throw new IllegalArgumentException("모든 테이블이 비어 있어야 합니다");
        }
        if (hasTableGroup()) {
            throw new IllegalArgumentException("이미 단체 지정이 되어 있습니다");
        }
        this.tableGroupId = tableGroupId;
    }

    public void unsetTableGroup() {
        this.tableGroupId = null;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
