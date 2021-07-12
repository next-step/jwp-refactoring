package kitchenpos.ordertable.domain;

import kitchenpos.exception.CannotCleanTableException;
import kitchenpos.tablegroup.domain.TableGroup;

import javax.persistence.*;

import static kitchenpos.common.Message.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public boolean cleanTable() {
        if (tableGroup != null) {
            throw new CannotCleanTableException(ERROR_ORDER_TABLE_CANNOT_BE_CLEANED_WHEN_GROUPED);
        }
        this.empty = true;
        return true;
    }

    public void updateNumberOfGuestsTo(int number) {
        if (number < 0) {
            throw new IllegalArgumentException(ERROR_TABLE_GUESTS_NUMBER_CANNOT_BE_SMALLER_THAN_ZERO.showText());
        }
        if (empty) {
            throw new IllegalArgumentException(ERROR_TABLE_GUESTS_NUMBER_CANNOT_BE_CHANGED_WHEN_EMPTY.showText());
        }
        this.numberOfGuests = number;
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void setTableGroupNew(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void setTableGroup(final Long tableGroupId) {
        this.tableGroup = tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

}
