package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = {CascadeType.PERSIST})
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    public OrderTable() {}

    private OrderTable(int numberOfGuests, boolean empty) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public void unGroupTable() {
        this.tableGroup = null;
    }

    public void registerGroupTable(TableGroup tableGroup) {
        validateGroupAndEmpty();
        this.tableGroup = tableGroup;
        tableGroup.addOrderTable(this);
    }

    public boolean hasGroup() {
        return Objects.nonNull(tableGroup);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        return tableGroup.getId();
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

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void updateNumberOfGuests(OrderTable updateTable) {
        this.numberOfGuests = updateTable.numberOfGuests;
    }

    public void updateEmpty(OrderTable updateTable) {
        this.empty = updateTable.empty;
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님수는 0 이상이어야 합니다.");
        }
    }

    private void validateGroupAndEmpty() {
        if (hasGroup()) {
            throw new IllegalArgumentException("이미 단체 지정이 되어있습니다.");
        }
        if (!isEmpty()) {
            throw new IllegalArgumentException("단체 지정은 빈 테이블이어야 합니다.");
        }
    }
}
