package kitchenpos.table.domain;

import kitchenpos.domain.Order;
import kitchenpos.table.message.OrderTableMessage;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    protected OrderTable() {

    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(Integer numberOfGuests, boolean empty) {
        return new OrderTable(NumberOfGuests.of(numberOfGuests), empty);
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

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty) {
        validateChangeEmpty();
        this.empty = empty;
    }

    private void validateChangeEmpty() {
        if(tableGroup != null) {
            throw new IllegalArgumentException(OrderTableMessage.CHANGE_EMPTY_ERROR_TABLE_GROUP_MUST_BE_NOT_ENROLLED.message());
        }

        if(this.orders.stream().anyMatch(order -> !order.isComplete())) {
            throw new IllegalArgumentException(OrderTableMessage.CHANGE_EMPTY_ERROR_INVALID_ORDER_STATE.message());
        }
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        validateChangeNumberOfGuests();
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
    }

    private void validateChangeNumberOfGuests() {
        if (this.empty) {
            throw new IllegalArgumentException(OrderTableMessage.CHANGE_GUESTS_ERROR_TABLE_MUST_BE_NOT_EMPTY_STATE.message());
        }
    }

    public void enrollGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
        order.enrollTable(this);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        OrderTable that = (OrderTable) o;

        if (empty != that.empty) return false;
        if (!Objects.equals(id, that.id)) return false;
        return Objects.equals(numberOfGuests, that.numberOfGuests);
    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (numberOfGuests != null ? numberOfGuests.hashCode() : 0);
        result = 31 * result + (empty ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                ", tableGroup=" + tableGroup +
                '}';
    }
}
