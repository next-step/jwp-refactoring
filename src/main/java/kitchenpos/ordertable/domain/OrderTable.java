package kitchenpos.ordertable.domain;

import java.util.Objects;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.common.error.CustomException;
import kitchenpos.common.error.ErrorInfo;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity
@Table(name = "order_table")
public class OrderTable {

    public OrderTable() {}

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    public OrderTable(Long id, NumberOfGuests numberOfGuests,  boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private OrderTable(NumberOfGuests numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(NumberOfGuests numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests.number());
    }

    public void checkExistsTableGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new CustomException(ErrorInfo.EXISTS_TABLE_GROUP);
        }
    }

    public void checkEmpty() {
        if (empty) {
            throw new CustomException(ErrorInfo.ORDER_TABLE_IS_EMPTY);
        }
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
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

    public int numberOfGuestsToInt() {
        return numberOfGuests.number();
    }

    public boolean isEmpty() {
        return empty;
    }

    public void empty(boolean empty) {
        this.empty = empty;
    }
}
