package kitchenpos.table.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private Long tableGroupId;

    @Column
    private int numberOfGuests;

    @Column
    private boolean empty;

    public OrderTable() { }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void groupingIn(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        empty = false;
    }

    public void ungrouping() {
        this.tableGroupId = null;
        empty = true;
    }

    public void validateForGrouping() {
        if (!isEmpty() || Objects.nonNull(getTableGroupId())) {
            throw new IllegalArgumentException("주문테이블이 그룹핑하기에 올바르지 않습니다.");
        }
    }

    public void isValidForOrdering() {
        if (isEmpty()) {
            throw new IllegalArgumentException("주문테이블에 손님이 없습니다.");
        }
    }

    public void checkIfAlreadyGrouped() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException();
        }
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void finishTable() {
        this.empty = true;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("주문테이블에 손님이 없습니다.");
        }

        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("주문테이블의 바꾸려는 손님 숫자가 올바르지 않습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
