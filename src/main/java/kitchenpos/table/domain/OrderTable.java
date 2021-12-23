package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.exception.NotSupportUngroupException;
import kitchenpos.table.exception.TableEmptyUpdateException;
import kitchenpos.table.exception.TableGuestNumberUpdateException;

import javax.persistence.*;
import java.util.Objects;

import static javax.persistence.FetchType.LAZY;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests = new NumberOfGuests();

    @Column(nullable = false)
    private boolean empty;

    @Embedded
    private final Orders orders = new Orders();

    protected OrderTable() {
    }

    public OrderTable(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public void changeEmpty(boolean changeEmpty) {
        if (checkTableGroup() || orders.checkOccupied()) {
            throw new TableEmptyUpdateException();
        }
        this.empty = changeEmpty;
    }

    private boolean checkTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    public void updateNumberOfGuests(Integer newNumberOfGuests) {
        if (empty) {
            throw new TableGuestNumberUpdateException();
        }

        this.numberOfGuests = NumberOfGuests.of(newNumberOfGuests);
    }

    public OrderTable addOrder(Order order) {
        orders.add(order);
        return this;
    }

    public OrderTable groupBy(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
        return this;
    }

    public void ungroup() {
        if (orders.checkOccupied()) {
            throw new NotSupportUngroupException();
        }
        tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }
}
