package kitchenpos.table.domain;

import java.util.Objects;
import java.util.Optional;
import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
        // empty
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void checkNotIncludeTableGroup() {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException();
        }
    }

    public void grouping(final TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        tableGroup.addOrderTable(this);
    }

    public void setEmpty(boolean empty) {
        this.empty = empty;
    }

    public void checkEmpty() {
        if (this.empty) {
            throw new IllegalArgumentException();
        }
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public Long getId() {
        return id;
    }

    public Optional<TableGroup> getTableGroup() {
        return Optional.ofNullable(tableGroup);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void ungroup() {
        this.tableGroup = null;
    }
}
