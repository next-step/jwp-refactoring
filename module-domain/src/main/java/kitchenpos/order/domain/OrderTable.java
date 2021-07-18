package kitchenpos.order.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
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

    @Embedded
    private Orders orders;

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
        ungroupValidate();
        this.tableGroupId = null;
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
        emptyValidate();
        this.empty = empty;
    }

    private void emptyValidate() {
        if (hasOtherOrderTable()) {
            throw new IllegalArgumentException("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
        }
        if (orders.isNotCompletion()) {
            throw new IllegalArgumentException("조리 또는 식사인 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
        }
    }

    public boolean hasOtherOrderTable() {
        return Objects.nonNull(tableGroupId);
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

    private void ungroupValidate() {
        if (orders.isNotCompletion()) {
            throw new IllegalArgumentException("아직 식사를 완료하지 않아, 단체 지정을 해지할 수 없습니다.");
        }
    }
}
