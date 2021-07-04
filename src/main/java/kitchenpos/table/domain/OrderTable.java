package kitchenpos.table.domain;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.exception.NotChangeEmptyException;
import kitchenpos.table.exception.NotChangeNumberOfGuestsException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    @Embedded
    private final Orders orders = new Orders();

    public OrderTable() {
        numberOfGuests = 0;
        empty = true;
    }

    public OrderTable(boolean empty) {
        numberOfGuests = 0;
        this.empty = empty;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(null, tableGroup, numberOfGuests, empty);
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    void group(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    void ungroup() {
        this.tableGroup = null;
    }

    public void changeEmpty(boolean empty) {

        if (tableGroup != null) {
            throw new NotChangeEmptyException("단체 지정된 주문 테이블은 빈 테이블 상태 변경이 불가능합니다.");
        }

        if (hasCookingOrMealOrder()) {
            throw new NotChangeEmptyException("요리 중이거나 식사 중인 주문이 있으면 빈 테이블 변경이 불가능합니다.");
        }

        this.empty = empty;
    }

    public void empty() {
        empty = true;
    }

    public void notEmpty() {
        empty = false;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (isEmpty()) {
            throw new NotChangeNumberOfGuestsException("빈 주문 테이블은 손님 수를 변경할 수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void addOrder(Order order) {

        if (isEmpty()) {
            throw new IllegalArgumentException();
        }

        this.orders.add(order);
        order.group(this);
    }

    public boolean hasCookingOrMealOrder() {
        return orders.hasCookingOrMealOrder();
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public Orders getOrders() {
        return orders;
    }
}
