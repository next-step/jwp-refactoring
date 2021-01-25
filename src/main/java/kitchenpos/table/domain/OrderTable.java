package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(mappedBy = "orderTable")
    private Order order;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public static OrderTable empty() {
        return new OrderTable(0, true);
    }

    public OrderTable(Long tableGroupId) {
        this(0, true);
        this.tableGroupId = tableGroupId;
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.id = id;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Order getOrder() {
        return order;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeStatus(boolean empty) {
        checkGrouping();
        checkOrderStatus();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        checkEmpty();
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수가 올바르지 않습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void addTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void checkOrderTable() {
        checkGrouping();
        checkNotEmpty();
    }

    public void checkEmpty() {
        if (empty) {
            throw new IllegalArgumentException("테이블이 비어있습니다.");
        }
    }

    public void checkNotEmpty() {
        if (!empty) {
            throw new IllegalArgumentException("테이블이 사용중입니다.");
        }
    }

    public void addOrder(Order order) {
        this.order = order;
    }

    public void checkOrderStatus() {
        if(Objects.nonNull(this.order) && !order.checkComplete()) {
            throw new IllegalArgumentException("주문이 완료되지 않았습니다.");
        }
    }

    private void checkGrouping() {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("그룹핑된 상태입니다.");
        }
    }
}
