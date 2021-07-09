package kitchenpos.table.domain;

import static java.util.Objects.*;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.tablegroup.domain.UngroupValidator;

@Entity
public class OrderTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    protected OrderTable() {}

    OrderTable(Long id, Long tableGroupId, NumberOfGuests numberOfGuests, boolean empty) {
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
        return Order.create(getId(), orderLineItems, orderedTime);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public Long getTableGroupId() {
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

    public void changeEmpty(boolean isEmpty, ChangeEmptyValidator changeEmptyValidator) {
        changeEmptyValidator.validate();
        this.empty = isEmpty;
    }

    public boolean isGrouped() {
        return nonNull(tableGroupId);
    }

    public void grouped(Long tableGroupId) {
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
