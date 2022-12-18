package kitchenpos.table.domain;

import javax.persistence.Column;
import javax.persistence.Entity;
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
    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;
    @Column(nullable = false)
    private int numberOfGuests;
    private boolean empty;


    protected OrderTable() {
    }

    protected OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        validNumberOfGuests(numberOfGuests);
        validNotEmptyTable();

        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(Boolean empty) {
        validTableGroupEmpty();
        setEmpty(empty);
    }

    public void validGroupingTableGroup() {
        validTableGroupEmpty();
        validEmptyTable();
    }

    public Long findTableGroupId() {
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        this.tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void notEmpty() {
        this.empty = false;
    }

    private void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    private void validNotEmptyTable() {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블 입니다.");
        }
    }

    private void validNumberOfGuests(Integer numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("0명 이상의 인원이 필요합니다.");
        }
    }

    private void validTableGroupEmpty() {
        if (tableGroup != null) {
            throw new IllegalArgumentException("단체되어 있습니다.");
        }
    }

    private void validEmptyTable() {
        if (!isEmpty()) {
            throw new IllegalArgumentException("빈테이블이 아닙니다.");
        }
    }
}
