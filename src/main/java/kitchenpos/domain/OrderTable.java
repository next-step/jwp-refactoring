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

import kitchenpos.exception.IllegalOperationException;
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
    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @Embedded
    private Orders orders = new Orders();

    protected OrderTable() {
    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this(null, numberOfGuests, empty);
    }

    OrderTable(Long id, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable withTableGroup(TableGroup tableGroup) {
        checkEmpty();
        checkNotGrouped();
        this.tableGroup = tableGroup;
        this.empty = false;
        return this;
    }

    public void addOrder(Order order) {
        checkNotEmpty();
        order.setTable(this);
        orders.add(order);
    }

    public void changeEmpty(boolean empty) {
        checkOrders();
        checkNotGrouped();
        this.empty = empty;
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        checkNotEmpty();
        this.numberOfGuests = numberOfGuests;
    }

    public void leaveTableGroup() {
        checkOrders();
        this.tableGroup = null;
    }

    private void checkOrders() {
        if (orders.hasOrderInProgress()) {
            throw new OrderNotCompletedException("테이블에 완결되지 않은 주문이 존재합니다.");
        }
    }

    private void checkEmpty() {
        if (!this.isEmpty()) {
            throw new IllegalOperationException("테이블이 비어있지 않습니다.");
        }
    }

    private void checkNotGrouped() {
        if (this.hasTableGroup()) {
            throw new IllegalArgumentException("테이블 그룹에 포함되어 있습니다.");
        }
    }

    private void checkNotEmpty() {
        if (this.isEmpty()) {
            throw new IllegalOperationException("빈 테이블 입니다.");
        }
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroup);
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

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
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
