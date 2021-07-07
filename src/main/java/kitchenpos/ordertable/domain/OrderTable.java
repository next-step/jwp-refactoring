package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private Orders orders;

    private int numberOfGuests;

    private boolean empty;

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = new Orders();
    }

    public OrderTable(final TableGroup tableGroup, final int numberOfGuests, final boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
        this.orders = new Orders();
    }

    public OrderTable() {

    }

    public void changeEmpty(boolean empty) {
        if (hasOtherOrderTable()) {
            throw new IllegalArgumentException("단체 지정된 주문 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
        }
        if (orders.isNotCompletion()) {
            throw new IllegalArgumentException("조리 또는 식사인 테이블은 빈 테이블 설정 또는 해지할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (empty) {
            throw new IllegalArgumentException("빈 테이블은 방문한 손님 수를 입력할 수 없습니다.");
        }
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문한 손님 수는 0명 이상이어야 합니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void putIntoGroup(TableGroup tableGroup) {
        if (tableGroup == null) {
            throw new IllegalArgumentException("tableGroup은 널이 아니어야합니다.");
        }

        if (this.tableGroup != null) {
            throw new IllegalArgumentException("OrderTable에 이미 TableGroup이 있습니다.");
        }

        if (!this.empty) {
            throw new IllegalArgumentException("OrderTable은 비어 있어야합니다.");
        }

        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public boolean hasOtherOrderTable() {
        return Objects.nonNull(tableGroup);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void ungroup() {
        ungroupValidate();
        tableGroup = null;
    }

    private void ungroupValidate() {
        if (orders.isNotCompletion()) {
            throw new IllegalArgumentException("아직 식사를 완료하지 않아, 단체 지정을 해지할 수 없습니다.");
        }
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
}