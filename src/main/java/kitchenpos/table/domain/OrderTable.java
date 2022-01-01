package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.table.dto.OrderTableChangeEmptyRequest;
import kitchenpos.table.exception.NotChangeEmptyException;
import kitchenpos.table.exception.NotChangeNumberOfGuestException;
import kitchenpos.table.exception.NotCreateTableGroupException;
import kitchenpos.table.exception.TableErrorCode;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private OrderTable(OrderTable orderTable, List<Order> orders) {
        this.id = orderTable.getId();
        this.tableGroup = orderTable.getTableGroup();
        this.orders = orders;
        this.empty = orderTable.empty;
        this.numberOfGuests = orderTable.getNumberOfGuests();
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable of(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        return new OrderTable(tableGroup, numberOfGuests, empty);
    }

    public static OrderTable create(Integer numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public static OrderTable of(OrderTable oldOrderTable, List<Order> orders) {
        return new OrderTable(oldOrderTable, orders);
    }

    public void availableCreate() {
        if (!isEmpty() || Objects.nonNull(getTableGroup())) {
            throw new NotCreateTableGroupException(TableErrorCode.ALREADY_ASSIGN_GROUP);
        }
    }

    public void changeEmpty(OrderTableValidator orderTableValidator, boolean empty) {
        orderTableValidator.validateChangeableEmpty(this);

        this.empty = empty;
    }

    public void changeNumberOfGuests(OrderTableValidator orderTableValidator, int updateNumberOfGuests) {
        orderTableValidator.validateChangeableNumberOfGuests(this);

        this.numberOfGuests = updateNumberOfGuests;
    }

    public boolean isGroupingTable() {
        return tableGroup != null;
    }

    public void assignTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void unGroup() {
        this.tableGroup = null;
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

    public List<Order> getOrders() {
        return orders;
    }
}
