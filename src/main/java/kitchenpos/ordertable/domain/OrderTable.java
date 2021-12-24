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
import kitchenpos.ordertable.exception.TableChangeNumberOfGuestsException;
import kitchenpos.ordertable.exception.TableUpdateStateException;

@Entity
public class OrderTable {

    private static final String ERROR_MESSAGE_TABLE_IN_GROUP = "테이블 그룹에 속해있는 테이블은 상태를 변경할 수 없습니다.";
    private static final String ERROR_MESSAGE_ORDER_NOT_FINISH = "주문 상태가 조리 혹은 식사인 주문이 존재합니다.";
    private final static String ERROR_MESSAGE_NUMBER_OF_GUESTS = "방문 손님 수는 0명 이상이어야 합니다.";
    private static final String ERROR_MESSAGE_CANNOT_CHANGE_NUM_OF_GUESTS_WHEN_ORDER_CLOSED = "주문 종료 상태에선 방문 손님 수를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean orderClose;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    protected OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public OrderTable(boolean orderClose) {
        this(0, orderClose);
    }

    public OrderTable(int numberOfGuests) {
        this(numberOfGuests, false);
    }

    public OrderTable(int numberOfGuests, boolean orderClose) {
        this(null, null, numberOfGuests, orderClose);
    }

    public OrderTable(Long id, int numberOfGuests, boolean orderClose) {
        this(id, null, numberOfGuests, orderClose);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean orderClose) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.orderClose = orderClose;
    }

    public void updateTableStatus(boolean orderClose) {
        if (Objects.nonNull(tableGroup)) {
            throw new TableUpdateStateException(ERROR_MESSAGE_TABLE_IN_GROUP);
        }

        for (Order order : orders) {
            validateAllOrdersComplete(order);
        }
        this.orderClose = orderClose;
    }

    private void validateAllOrdersComplete(Order order) {
        if (!order.isCompleteStatus()) {
            throw new TableUpdateStateException(ERROR_MESSAGE_ORDER_NOT_FINISH);
        }
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new TableChangeNumberOfGuestsException(ERROR_MESSAGE_NUMBER_OF_GUESTS);
        }

        if (isOrderClose()) {
            throw new TableChangeNumberOfGuestsException(
                ERROR_MESSAGE_CANNOT_CHANGE_NUM_OF_GUESTS_WHEN_ORDER_CLOSED);
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void groupIn(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    public void changeOrderClose(final boolean orderClose) {
        this.orderClose = orderClose;
    }

    public boolean isOrderClose() {
        return orderClose;
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

    public List<Order> getOrders() {
        return orders;
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
}
