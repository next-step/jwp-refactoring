package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(columnDefinition = "bigint(20)")
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;

    @Column(nullable = false, columnDefinition = "int(11)")
    private int numberOfGuests;

    @Column(nullable = false, columnDefinition = "bit(1)")
    private boolean empty;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, int numberOfGuests, TableGroup tableGroup) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.tableGroup = tableGroup;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void toGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        validateEmptyChangeable();
        this.empty = empty;
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroup);
    }

    public List<Order> getOrders() {
        return orders;
    }

    public boolean hasOrderWithStatus(OrderStatus orderStatus) {
        return orders.stream()
            .anyMatch(order -> order.isOrderStatus(orderStatus));
    }

    private void validateEmptyChangeable() {
        if (isGrouped()) {
            throw new IllegalArgumentException("단체 지정되어 있는 테이블은 빈 테이블 변경을 할 수 없습니다.");
        }
        if (hasOrderWithStatus(OrderStatus.COOKING) || hasOrderWithStatus(OrderStatus.MEAL)) {
            throw new IllegalArgumentException("MEAL 이나 COOKING 상태의 주문이 있으면 빈 테이블 변경을 할 수 없습니다.");

        }
    }
}
