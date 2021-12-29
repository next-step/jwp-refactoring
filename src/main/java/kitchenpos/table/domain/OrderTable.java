package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @OneToOne(fetch = FetchType.LAZY)
    private Order order;

    protected OrderTable() {
    }

    public OrderTable(Order order) {
        this.order = order;
    }

    public OrderTable(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty, Order order) {
        this(numberOfGuests, empty);
        this.order = order;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.tableGroup = tableGroup;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty, Order order) {
        return new OrderTable(numberOfGuests, empty, order);
    }

    public void empty() {
        if (this.tableGroup != null) {
            throw new IllegalArgumentException("이미 단체로 지정되어 있어서 빈 테이블로 변경할 수 없습니다.");
        }

        if (this.order == null) {
            this.empty = true;
            return;
        }

        if (this.order.isEqualToOrderStatus(OrderStatus.COOKING) ||
                this.order.isEqualToOrderStatus(OrderStatus.MEAL)) {
            throw new IllegalArgumentException("조리 또는 식사 중이면 빈 테이블로 변경할 수 없습니다.");
        }

        this.empty = true;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("변경할 방문 손님수가 0보다 작습니다.");
        }

        if (empty) {
            throw new IllegalArgumentException("해당 주문 테이블은 빈 테이블입니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void applyTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void ungroup() {
        if (order.isEqualToOrderStatus(OrderStatus.COOKING) ||
                order.isEqualToOrderStatus(OrderStatus.MEAL)) {
            throw new IllegalArgumentException("조리 또는 식사 중인 주문 테이블이 존재합니다.");
        }
        this.tableGroup = null;
    }

    public void updateOrder(Order order) {
        this.order = order;
    }

    public boolean existTableGroup() {
        return Objects.nonNull(this.tableGroup);
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

    public Order getOrder() {
        return order;
    }
}
