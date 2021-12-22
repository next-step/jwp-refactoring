package kitchenpos.order.domain;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    private TableGroup tableGroup;
    @Embedded
    private NumberOfGuests numberOfGuests;
    private boolean empty;
    @Embedded
    private Orders orders = new Orders();

    public OrderTable() {
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty, List<Order> orders) {
        this(id, tableGroup, numberOfGuests, empty);
        this.orders = new Orders(orders);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public OrderTable(boolean empty) {
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("비어 있는 테이블은 손님 수를 변경할 수 없습니다");
        }
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    public void changeEmptyStatus(final boolean empty) {
        if (containsStartedOrder()) {
            throw new IllegalArgumentException("주문이 진행되어 테이블 비움 상태를 변경할 수 없습니다");
        }
        this.empty = empty;
    }

    public void changeToNotEmpty() {
        this.empty = false;
    }

    public boolean hasTableGroup() {
        return tableGroup != null;
    }

    public boolean containsStartedOrder() {
        return orders.containsStartedOrder();
    }

    public void setTableGroup(TableGroup tableGroup) {
        if (!isEmpty()) {
            throw new IllegalArgumentException("모든 테이블이 비어 있어야 합니다");
        }
        if (hasTableGroup()) {
            throw new IllegalArgumentException("이미 단체 지정이 되어 있습니다");
        }
        this.tableGroup = tableGroup;
    }

    public void unsetTableGroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
