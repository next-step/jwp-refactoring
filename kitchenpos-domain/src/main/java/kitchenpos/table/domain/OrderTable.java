package kitchenpos.table.domain;

import kitchenpos.table.exception.NotChangeToEmptyThatGroupTable;
import kitchenpos.table.exception.NotChangeNumberOfGuestThatEmptyTable;
import kitchenpos.table.validator.OrderTableValidator;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Column(name = "order_id")
    private Long orderId;

    private int numberOfGuests;
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    public OrderTable(int numberOfGuests) {
        this(null, numberOfGuests);
    }

    public void registerOrder(Long orderId) {
        this.orderId = orderId;
    }

    public void validateToEmpty(OrderTableValidator orderTableValidator) {
        if (tableGroup != null) {
            throw new NotChangeToEmptyThatGroupTable();
        }
        orderTableValidator.validateToEmpty(this);
        makeEmptyTable();
    }

    public void registerTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        if (tableGroup != null) {
            this.empty = false;
        }
    }

    private void makeEmptyTable() {
        this.empty = true;
    }

    public boolean isEmpty() {
        return this.empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException();
        }
        if (empty) {
            throw new NotChangeNumberOfGuestThatEmptyTable();
        }
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmptyTableAndNotExistTableGroupId() {
        return this.empty || this.tableGroup == null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getOrderId() {
        return this.orderId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeToEmpty() {
        if (tableGroup != null) {
            throw new NotChangeToEmptyThatGroupTable();
        }
        this.empty = true;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroup, that.tableGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, numberOfGuests, empty);
    }
}
