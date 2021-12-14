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
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.domain.tablegroup.TableGroup;

@Entity
@Table(name = "order_table")
public class OrderTable {
    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected OrderTable() {}

    private OrderTable(Long id) {
        this.id = id;
    }

    private OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.id = id;
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = empty;
    }

    public static OrderTable from(Long id) {
        return new OrderTable(id);
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

    public void alignTableGroup(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        updateEmpty(false);
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public void updateEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroup);
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
}
