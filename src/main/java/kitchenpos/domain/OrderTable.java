package kitchenpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.BatchSize;

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
    private TableGroup tableGroup;
    @JsonIgnore
    @BatchSize(size = 5)
    @OneToMany(mappedBy = "orderTable")
    private final List<Order> orders = new ArrayList<>();
    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("고객 수가 올바르지 않습니다.");
        }

        if (isEmpty()) {
            throw new IllegalArgumentException("해당테이블은 비어있는 상태입니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("테이블 그룹이 지정되어 있습니다.");
        }

        if (isNotCompletedOrders()) {
            throw new IllegalArgumentException("아직 완료되지 않은 주문이 존재합니다.");
        }

        this.empty = empty;
    }

    private boolean isNotCompletedOrders() {
        return orders.stream().anyMatch(order -> isNotCompleted(order.getOrderStatus()));
    }

    private boolean isNotCompleted(final String orderStatus) {
        return orderStatus.equals(OrderStatus.COOKING.name()) || orderStatus.equals(OrderStatus.MEAL.name());
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void group(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void addOrder(final Order order) {
        this.orders.add(order);
    }

    public void unGroup() {
        tableGroup = null;
    }
}
