package kitchenpos.domain.table;

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
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private Long tableGroupId;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Column(name = "empty", nullable = false)
    private boolean empty;

    protected OrderTable() {}

    private OrderTable(boolean empty, int numberOfGuests) {
        this.empty = empty;
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    public static OrderTable of(boolean empty, int numberOfGuests) {
        return new OrderTable(empty, numberOfGuests);
    }

    public Long getId() {
        return id;
    }

    public int findNumberOfGuests() {
        return numberOfGuests.getValue();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void mappedByTableGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void changeEmpty(boolean empty) {
        this.empty = empty;
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
    }

    public Long getTableGroupId() {
        return this.tableGroupId;
    }
}
