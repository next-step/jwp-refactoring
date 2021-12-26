package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.exception.NotSupportUngroupException;
import kitchenpos.table.exception.TableEmptyUpdateException;
import kitchenpos.table.exception.TableGuestNumberUpdateException;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests = new NumberOfGuests();

    @Column(nullable = false)
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Integer numberOfGuests, boolean empty, Long tableGroupId) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = empty;
        this.tableGroupId = tableGroupId;
    }

    public void updateNumberOfGuests(Integer newNumberOfGuests) {
        if (empty) {
            throw new TableGuestNumberUpdateException();
        }
        this.numberOfGuests = NumberOfGuests.of(newNumberOfGuests);
    }

    public OrderTable groupBy(TableGroup tableGroup) {
        this.empty = false;
        return this;
    }

    public boolean isGrouped() {
        return Objects.nonNull(tableGroupId);
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }
}
