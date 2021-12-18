package kitchenpos.domain.table;

import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Table;

@Entity
@Table(name = "order_table")
public class OrderTable {
    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected OrderTable() {}

    private OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.id = id;
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable from(Long id) {
        return new OrderTable(id, 0, true);
    }

    public static OrderTable from(int numberOfGuests) {
        boolean isEmpty = numberOfGuests == MIN_NUMBER_OF_GUESTS;
        return new OrderTable(numberOfGuests, isEmpty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable of(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public void alignTableGroup(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        updateEmpty(false);
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }
}
