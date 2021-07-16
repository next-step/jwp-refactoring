package kitchenpos.ordertable.domain;

import kitchenpos.exception.CannotUpdateException;

import javax.persistence.*;

import static kitchenpos.common.Message.*;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private Long tableGroupId;

    @Column(name = "number_of_guests", nullable = false)
    private int numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean empty = true;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(0L, null, numberOfGuests, empty);
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        this(0L, tableGroupId, numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(boolean isEmpty) {
        if (tableGroupId != null) {
            throw new CannotUpdateException(ERROR_ORDER_TABLE_CANNOT_BE_CLEANED_WHEN_GROUPED);
        }
        this.empty = isEmpty;
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

    public void assignToTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public boolean isAssignedToTableGroup() {
        return tableGroupId != null;
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
