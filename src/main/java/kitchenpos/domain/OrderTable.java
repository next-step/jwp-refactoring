package kitchenpos.domain;

import java.util.Objects;
import java.util.Optional;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import kitchenpos.exception.OrderNotCompletedException;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @Embedded
    private Orders orders = new Orders();

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(id,null, numberOfGuests, empty);
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

    public Optional<TableGroup> getTableGroup() {
        return Optional.ofNullable(tableGroup);
    }

    public Orders getOrders() {
        return orders;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty) {
        checkOrders();
        checkTableGroup();
        this.empty = empty;
    }

    private void checkTableGroup() {
        if (this.hasTableGroup()) {
            throw new IllegalArgumentException("테이블 그룹에 포함되어 있습니다.");
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        checkTableIsEmpty();
        checkNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void checkTableIsEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블의 손님 수를 변경할 수 없습니다.");
        }
    }

    private void checkNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블의 손님 수는 음수가 될 수 없습니다.");
        }
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    public void setTableGroup(TableGroup tableGroup) {
        checkTableGroup();
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        checkOrders();
        this.tableGroup = null;
    }

    private void checkOrders() {
        if (orders.hasOrderInProgress()) {
            throw new OrderNotCompletedException("테이블에 완결되지 않은 주문이 존재합니다.");
        }
    }

    public void addOrder(Order order) {
        checkTableStatus();
        order.setTable(this);
        orders.add(order);
    }

    private void checkTableStatus() {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블에는 주문을 넣을 수 없습니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderTable that = (OrderTable)o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
