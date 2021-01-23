package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.ordertablegroup.domain.OrderTableGroup;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity(name = "order_table")
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private OrderTableGroup orderTableGroup;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(OrderTableGroup orderTableGroup, int numberOfGuests, boolean empty) {
        this.orderTableGroup = orderTableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public OrderTableGroup getOrderTableGroup() {
        return orderTableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Long getTableGroupId() {
        return orderTableGroup == null ? null : orderTableGroup.getId();
    }

    public void setOrderTableGroup(OrderTableGroup orderTableGroup) {
        this.orderTableGroup = orderTableGroup;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        validateOrdersStatus();
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroupTable() {
        validateOrdersStatus();
        this.orderTableGroup = null;
    }

    public boolean isNotAvailableOrderTable() {
        return Objects.nonNull(this.getOrderTableGroup());
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("게스트 인원이 0보다 커야한다.");
        }
    }

    private void validateOrdersStatus() {
        Optional<Order> order = this.orders.stream()
            .filter(Order::isNotCompleteStatus)
            .findFirst();
        if (order.isPresent()) {
            throw new IllegalArgumentException("`조리중`과 `식사중`에는 변경할 수 없다.");
        }
    }
}
