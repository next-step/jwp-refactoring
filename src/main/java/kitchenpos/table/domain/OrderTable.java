package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.domain.exception.UngroupTableException;
import kitchenpos.table.domain.exception.CannotOrderEmptyTableException;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private OrderTableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Orders orders;

    @Column(name = "empty")
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Long id, OrderTableGroup tableGroup, NumberOfGuests numberOfGuests, Orders orders, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.orders = orders;
        this.empty = empty;
    }

    public static OrderTable of(Long id, OrderTableGroup tableGroup, int numberOfGuests, List<Order> orders, boolean empty) {
        return new OrderTable(id, tableGroup, NumberOfGuests.of(numberOfGuests), Orders.of(orders), empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, NumberOfGuests.of(numberOfGuests), Orders.of(new ArrayList<>()), empty);
    }

    public void changeEmpty(boolean empty) {
        validateTableGroup();
        validateNotCompletionStatus();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateOrderable();
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public void registerGroup(OrderTableGroup orderTableGroup) {
        this.tableGroup = orderTableGroup;
    }

    public void validateOrderable() {
        if (empty) {
            throw new CannotOrderEmptyTableException();
        }
    }

    public void validateTableGroupable() {
        validateTableGroup();
        if (!empty) {
            throw new UngroupTableException("테이블 그룹을 지으려면 주문 불가능 상태여야합니다.");
        }
    }

    private void validateTableGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new UngroupTableException("이미 그룹화된 테이블입니다.");
        }
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public void validateNotCompletionStatus(){
        orders.validateAllNotCompletionStatus();
    }

    public void addOrder(Order order){
        orders.add(order);
    }

    public Long getId() {
        return id;
    }

    public OrderTableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
