package kitchenpos.table.domain;

import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {
    public static final int MIN_NUMBER_OF_GUESTS = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        checkNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty) {
        checkIsSetTableGroup();
        this.empty = empty;
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        checkNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroup() {
        empty = false;
        tableGroup = null;
    }

    private void checkIsSetTableGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체지정이 되어있는 테이블은 빈 테이블로 변경할 수 없습니다");
        }
    }

    private void checkNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException(String.format("테이블의 손님 수는 최소 %d명 이상이어야합니다", MIN_NUMBER_OF_GUESTS));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
