package kitchenpos.ordertable.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {

    public static final String MESSAGE_VALIDATE_ORDER_TABLE_CHANGABLE = "테이블이 비어있지 않아야 합니다.";
    public static final String MESSAGE_VALIDATE_EMPTY_CHANGABLE = "테이블 그룹에 추가되어 있지 않아야 합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;

    @Embedded
    @Column(nullable = false)
    private NumberOfGuests numberOfGuests;

    @Embedded
    @Column(nullable = false)
    private Empty empty;

    @Embedded
    private Orders orders = new Orders();

    protected OrderTable() {
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new Empty(empty);
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new Empty(empty);
    }

    public OrderTable(boolean empty) {
        this.empty = new Empty(empty);
    }

    public OrderTable(int numberOfGuests) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public Empty isEmpty() {
        return empty;
    }

    public Orders getOrders() {
        return orders;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        tableGroup.addToOrderTables(this);
        this.tableGroup = tableGroup;
    }

    public void changeEmpty(boolean empty) {
        validateEmptyChangable();
        this.empty = new Empty(empty);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validateNumberOfGuestsChangable();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void ungroup() {
        tableGroup = null;
    }

    public boolean isGroupable() {
        return empty.isGroupable() && Objects.isNull(tableGroup);
    }

    public boolean isChangable() {
        return orders.isChangable();
    }

    public void addToOrders(Order order) {
        orders.add(order);
    }

    public void validateOrderTableChangable() {
        if (empty.isEmpty()) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_ORDER_TABLE_CHANGABLE);
        }
    }

    private void validateEmptyChangable() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException(MESSAGE_VALIDATE_EMPTY_CHANGABLE);
        }
        orders.validateEmptyChangable();
    }

    private void validateNumberOfGuestsChangable() {
        empty.validateNumberOfGuestsChangable();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id) && Objects.equals(tableGroup, that.tableGroup) && Objects.equals(numberOfGuests, that.numberOfGuests) && Objects.equals(empty, that.empty);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, numberOfGuests, empty);
    }
}
