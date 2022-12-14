package kitchenpos.ordertable.domain;

import java.time.LocalDateTime;
import java.util.Arrays;
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
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.ordertable.exception.CannotChangeEmptyException;
import kitchenpos.ordertable.exception.CannotChangeNumberOfGuestsException;
import kitchenpos.exception.ExceptionMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.Orders;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private Orders orders = Orders.createEmpty();

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = Optional.ofNullable(tableGroupId)
                .map(it -> TableGroup.of(it, LocalDateTime.now()))
                .orElse(null);
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, null, numberOfGuests, empty);
    }

    public static OrderTable of(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return Optional.ofNullable(tableGroup)
                .map(TableGroup::getId)
                .orElse(null);
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroup = Optional.ofNullable(tableGroupId)
                .map(it -> TableGroup.of(it, LocalDateTime.now()))
                .orElse(null);
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (this.isEmpty()) {
            throw new CannotChangeNumberOfGuestsException(ExceptionMessage.CAN_NOT_CHANGE_NUMBER_OF_GUESTS);
        }

        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {

        if (Objects.nonNull(this.getTableGroupId())) {
            throw new CannotChangeEmptyException(ExceptionMessage.CAN_NOT_CHANGE_EMPTY_WHEN_TABLE_GROUPED);
        }

        if (orders.anyMatchedIn(Arrays.asList(OrderStatus.COOKING, OrderStatus.MEAL))) {
            throw new CannotChangeEmptyException(ExceptionMessage.CAN_NOT_CHANGE_EMPTY_WHEN_COOKING_OR_MEAL);
        }

        this.empty = empty;
    }

    public Orders getOrders() {
        return orders;
    }

    public void addOrder(Order order) {
        this.orders.add(order);
    }

    public void registerTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    public boolean isGrouping() {
        return this.tableGroup == null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        OrderTable that = (OrderTable) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
