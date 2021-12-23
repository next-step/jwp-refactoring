package kitchenpos.ordertable.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import kitchenpos.order.domain.Order;

@Entity
public class OrderTable {

    private static final String ERROR_MESSAGE_TABLE_IN_GROUP = "테이블 그룹에 속해있는 테이블은 상태를 변경할 수 없습니다.";
    private static final String ERROR_MESSAGE_ORDER_NOT_FINISH = "주문 상태가 조리 혹은 식사인 주문이 존재합니다.";
    private final static String ERROR_MESSAGE_NUMBER_OF_GUESTS = "방문 손님 수는 0명 이상이어야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    public OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public OrderTable(boolean empty) {
        this(0, empty);
    }

    public OrderTable(int numberOfGuests) {
        this(numberOfGuests, false);
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void groupIn(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof OrderTable)) {
            return false;
        }

        OrderTable that = (OrderTable) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    public void updateEmpty(boolean updataEmpty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException(ERROR_MESSAGE_TABLE_IN_GROUP);
        }

        for (Order order : orders) {
            validateAllOrdersComplete(order);
        }
        empty = updataEmpty;
    }

    private void validateAllOrdersComplete(Order order) {
        if (!order.isCompleteStatus()) {
            throw new IllegalArgumentException(ERROR_MESSAGE_ORDER_NOT_FINISH);
        }
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException(ERROR_MESSAGE_NUMBER_OF_GUESTS);
        }

        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void unGroup() {
        this.tableGroup = null;
    }
}
