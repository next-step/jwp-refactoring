package kitchenpos.ordertable.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    private int numberOfGuests;
    private boolean empty;

    private static final int MIN_GUEST_NUMBER = 0;

    protected OrderTable() {
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        validateOrderTable();
        this.numberOfGuests = numberOfGuests;
    }
    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_GUEST_NUMBER) {
            throw new IllegalArgumentException();
        }
    }

    private void validateOrderTable() {
        if (isEmpty()) {
            throw new IllegalArgumentException();
        }
    }
    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        validateGrouped();
        this.empty = empty;
    }

    private void validateGrouped() {
        if (isGrouped()) {
            throw new IllegalArgumentException();
        }
    }

    public boolean isGrouped() {
        return tableGroup != null;
    }

    public void groupBy(TableGroup tableGroup){
        if (tableGroup == null) {
            throw new IllegalArgumentException();
        }
        this.tableGroup = tableGroup;
        empty = false;
    }



    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void ungroup(){
        this.tableGroup = null;
    }
}
