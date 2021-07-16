package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.exception.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
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
        validateChangeEmpty();
        this.empty = empty;
    }

    private void validateChangeEmpty() {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new CannotChangeEmptyException("이미 그룹화된 테이블은 주문가능상태를 변경할 수 없습니다.");
        }
        if (orders.hasCookingOrMeal()) {
            throw new CannotChangeEmptyException("주문 테이블에 속한 주문 중에 진행중인 것이 있습니다.");
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateChangeNumberOfGuests();
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    private void validateChangeNumberOfGuests() {
        if (empty) {
            throw new CannotChangeGuestEmptyTableException();
        }
    }

    public void registerGroup(Long tableGroupId) {
        validateRegisterGroup(tableGroupId);
        this.empty = false;
        this.tableGroupId = tableGroupId;
    }

    private void validateRegisterGroup(Long tableGroupId) {
        if (Objects.isNull(tableGroupId)) {
            throw new CannotRegisterGroupException("그룹화하려는 테이블 그룹의 아이디가 null 입니다.");
        }
        if (Objects.nonNull(this.tableGroupId)) {
            throw new CannotRegisterGroupException("이미 그룹화된 테이블입니다.");
        }
        if (!empty) {
            throw new CannotRegisterGroupException("테이블 그룹을 지으려면 주문 불가능 상태여야합니다.");
        }
    }

    public void ungroup() {
        validateUpgroup();
        this.tableGroupId = null;
    }

    private void validateUpgroup() {
        if (orders.hasCookingOrMeal()) {
            throw new CannotUngroupException();
        }
    }

    public Order ordered(OrderLineItems orderLineItems) {
        validateOrdered();
        return orders.newOrder(getId(), orderLineItems);
    }

    private void validateOrdered() {
        if (empty) {
            throw new CannotOrderEmptyTableException();
        }
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
