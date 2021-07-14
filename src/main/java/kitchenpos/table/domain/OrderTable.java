package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.exception.CannotOrderEmptyTableException;
import kitchenpos.table.domain.exception.InvalidOrderTableException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Orders orders;

    @Column(name = "empty")
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Long id, Long tableGroupId, NumberOfGuests numberOfGuests, Orders orders, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.orders = orders;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, NumberOfGuests.of(numberOfGuests), Orders.of(new ArrayList<>()), empty);
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new InvalidOrderTableException("이미 그룹화된 테이블은 주문가능상태를 변경할 수 없습니다.");
        }
        validateNotCompletionStatus();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateOrderable();
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public void registerGroup(Long tableGroupId) {
        if (Objects.isNull(tableGroupId)) {
            throw new InvalidOrderTableException("그룹화하려는 테이블 그룹의 아이디가 null 입니다.");
        }

        if (Objects.nonNull(this.tableGroupId)) {
            throw new InvalidOrderTableException("이미 그룹화된 테이블입니다.");
        }

        if (!empty) {
            throw new InvalidOrderTableException("테이블 그룹을 지으려면 주문 불가능 상태여야합니다.");
        }

        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    public void validateOrderable() {
        if (empty) {
            throw new CannotOrderEmptyTableException();
        }
    }

    public void ungroup() {
        validateNotCompletionStatus();
        this.tableGroupId = null;
    }

    public void validateNotCompletionStatus() {
        orders.validateAllNotCompletionStatus();
    }

    public void addOrder(Order order) {
        orders.add(order);
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
}
