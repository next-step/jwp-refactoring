package kitchenpos.table.domain;

import kitchenpos.table.dto.OrderTableRequest;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long tableGroupId;
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    private void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if(numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 0보다 작을수 없습니다.");
        }

        if(isEmpty()) {
            throw new IllegalArgumentException("빈테이블은 손님의 수를 변경할수 없습니다.");
        }

        setNumberOfGuests(numberOfGuests);
    }

    public void changeEmpty(OrderTableRequest orderTableRequest) {
        if (Objects.nonNull(getTableGroupId())) {
            throw new IllegalArgumentException("단체테이블인 경우 테이블을 비울수 없습니다.");
        }

        setEmpty(orderTableRequest.isEmpty());
    }

    public boolean isUnableTableGroup() {
        if (!isEmpty() || Objects.nonNull(getTableGroupId())) {
            return true;
        }
        return false;
    }

    public void ungroup() {
        setTableGroupId(null);
    }
}
