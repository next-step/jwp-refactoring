package kitchenpos.tablegroup.domain;


import kitchenpos.tablegroup.exception.TableExceptionConstants;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Objects;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;

    @Embedded
    private OrderTableGuests numberOfGuests = new OrderTableGuests();

    @Embedded
    private OrderTableEmpty empty = new OrderTableEmpty();

    protected OrderTable() {}

    public OrderTable(int numberOfGuests, boolean empty) {
        this.tableGroup = null;
        this.numberOfGuests = new OrderTableGuests(numberOfGuests);
        this.empty = new OrderTableEmpty(empty);
    }

    void updateTableGroup(TableGroup tableGroup) {
        if(tableGroup.isDifferent(this.tableGroup)) {
            this.tableGroup = tableGroup;
            tableGroup.addOrderTable(this);
        }
    }

    public void checkOrderTableForTableGrouping() {
        this.empty.validateForTableGrouping();
    }

    private void checkTableGroup() {
        if(Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException(TableExceptionConstants.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getErrorMessage());
        }
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public void changeEmpty(boolean empty) {
        checkTableGroup();
        this.empty = this.empty.changeEmpty(empty);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = this.numberOfGuests.changeNumberOfGuests(numberOfGuests, isEmpty());
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
