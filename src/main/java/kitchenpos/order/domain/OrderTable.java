package kitchenpos.order.domain;

import kitchenpos.order.application.exception.InvalidTableState;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.GenerationType.IDENTITY;
import static kitchenpos.order.domain.OrderStatus.READY;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long tableGroupId;

    private int numberOfGuests;

    @Embedded
    private TableState tableState;

    @Enumerated(value = STRING)
    private OrderStatus orderStatus;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, TableState tableState) {
        this.numberOfGuests = numberOfGuests;
        this.tableState = tableState;
        this.orderStatus = READY;
    }

    public void group(Long newTableGroup) {
        if (Objects.nonNull(tableGroupId)) {
            throw new InvalidTableState("테이블에 일행이 있습니다.");
        }
        this.tableGroupId = newTableGroup;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public void changeGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty() {
        this.tableState = new TableState(true);
        this.numberOfGuests = 0;
    }

    public void changeStatus(OrderStatus orderStatus) {
        this.orderStatus = orderStatus;
    }

    public boolean isCompleted() {
        return orderStatus == OrderStatus.COMPLETION;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroupId)) {
            return null;
        }
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public TableState getTableState() {
        return tableState;
    }

    public OrderStatus getOrderStatus() {
        return orderStatus;
    }

    public boolean isEmpty() {
        return tableState.isEmpty();
    }
}
