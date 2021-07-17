package kitchenpos.order.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.advice.exception.OrderTableException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public void validateTableGroupIsNull() {
        if (this.tableGroupId != null) {
            throw new OrderTableException("테이블 그룹은 비어있어야 합니다");
        }
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

    public void addTableGroup(Long tableGroupId) {
        updateEmpty(false);
        this.tableGroupId = tableGroupId;
    }

    public void updateEmpty(boolean empty) {
        this.empty = empty;
    }

    public void updateNumberOfGuests(int numberOfGuests) {
        validateOrderTableIsEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    private void validateOrderTableIsEmpty() {
        if (isEmpty()) {
            throw new OrderTableException("주문 테이블이 비어있습니다");
        }
    }
}
