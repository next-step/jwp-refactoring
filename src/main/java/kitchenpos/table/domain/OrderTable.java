package kitchenpos.table.domain;

import kitchenpos.exception.OrderTableException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    private static final String NOT_CHANGE_GROUP_TABLE_ERROR_MESSAGE = "그룹핑 되어있는 테이블 상태를 변경할 수 없습니다.";
    private static final String NOT_CHANGE_EMPTY_TABLE_ERROR_MESSAGE = "비어있는 테이블의 인원수를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    @Embedded
    private NumberOfGuests numberOfGuests = new NumberOfGuests();
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests =new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public void checkValidEmptyTableGroup() {
        if (Objects.nonNull(tableGroupId)) {
            throw new OrderTableException(NOT_CHANGE_GROUP_TABLE_ERROR_MESSAGE);
        }
    }

    public void checkIsEmpty() {
        if (empty) {
            throw new OrderTableException(NOT_CHANGE_EMPTY_TABLE_ERROR_MESSAGE);
        }
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
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

    public void withTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    @Override
    public boolean equals(Object object) {
        if (this == object) return true;
        if (object == null || getClass() != object.getClass()) return false;
        OrderTable that = (OrderTable) object;
        return empty == that.empty &&
                Objects.equals(id, that.id) &&
                Objects.equals(tableGroupId, that.tableGroupId) &&
                Objects.equals(numberOfGuests, that.numberOfGuests);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroupId, numberOfGuests, empty);
    }
}
