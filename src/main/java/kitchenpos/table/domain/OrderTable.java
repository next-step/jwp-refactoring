package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;

import javax.persistence.*;

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
    private Orders orders;

    protected OrderTable() {
        this.empty = true;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = new Orders();
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.tableGroupId = tableGroupId;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public Orders getOrders() {
        return orders;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수는 0보다 큰 값을 입력해 주세요.");
        }
        if (this.empty) {
            throw new IllegalArgumentException("빈 테이블에는 게스트를 입력할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(boolean empty) {
        if (tableGroupId != null) {
            throw new IllegalArgumentException("단체 지정된 테이블은 빈 테이블 설정/해지할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void ungroup() {
        if(!orders.checkChangeable()){
            throw new IllegalArgumentException("요리중이거나 식사중인 테이블은 변경할 수 없습니다.");
        }
        this.tableGroupId = null;
    }

    public boolean isEmpty() {
        return empty;
    }
}
