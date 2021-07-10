package kitchenpos.table.domain;

import static java.util.Objects.*;

import java.time.LocalDateTime;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderTableId;
import kitchenpos.tablegroup.domain.UngroupValidator;

@Entity
public class OrderTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Embedded
    private TableGroupId tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {}

    OrderTable(Long id, TableGroupId tableGroupId, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(NumberOfGuests.valueOf(numberOfGuests), empty);
    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public Order createOrder(OrderLineItems orderLineItems, LocalDateTime orderedTime) {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈테이블에서 주문할 수 없습니다.");
        }
        return new Order(new OrderTableId(getId()), orderLineItems, orderedTime);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public TableGroupId getTableGroupId() {
        return tableGroupId;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 방문 손님 수를 수정할 수 없습니다.");
        }
        this.numberOfGuests = NumberOfGuests.valueOf(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty;
    }
        public void changeEmpty(boolean isEmpty, ChangeEmptyExternalValidator externalValidator) {
        externalValidator.validate(getId());
        if (isGrouped()) {
            throw new IllegalArgumentException("그룹 설정이 되어 있는 테이블은 주문 등록 불가 상태로 바꿀 수 없습니다.");
        }
        this.empty = isEmpty;
    }

    public boolean isGrouped() {
        return nonNull(tableGroupId);
    }

    public void grouped(TableGroupId tableGroupId) {
        this.tableGroupId = tableGroupId;
        emptyOff();
    }

    public void ungrouped(UngroupValidator ungroupValidator) {
        ungroupValidator.validate(this);
        this.tableGroupId = null;
    }

    private void emptyOff() {
        this.empty = false;
    }
}
