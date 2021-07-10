package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.exception.NotChangeEmptyException;
import kitchenpos.table.exception.NotChangeNumberOfGuestsException;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

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

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(null, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    void group(TableGroup tableGroup) {
        this.tableGroupId = tableGroup.getId();
    }

    void ungroup() {
        this.tableGroupId = null;
    }

    public void changeEmpty(boolean empty) {

        if (tableGroupId != null) {
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
            throw new IllegalArgumentException("빈 상태의 주문 테이블에는 주문을 추가할 수 없습니다.");
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

    public Long getTableGroupId() {
        return tableGroupId;
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
