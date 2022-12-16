package kitchenpos.table.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import java.util.Objects;

@Entity
@Table(name = "order_table")
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JoinColumn(name = "table_group_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private TableGroup tableGroup;
    @Column(name = "number_of_guests")
    private int numberOfGuests;
    @Column
    private boolean empty;

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable ofNumberOfGuestsAndEmpty(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    protected OrderTable() {
    }

    public Long getId() {
        return id;
    }

    @JsonIgnore
    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty) {
        checkGroup();
        this.empty = empty;
    }

    public void changeNumberOfGuest(int numberOfGuests) {
        checkEmptyTable();
        this.numberOfGuests = numberOfGuests;
    }

    public void checkGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체 지정 값이 없어야 합니다");
        }
    }

    public void checkValidGuestNumber() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("방문 고객 수는 0보다 작을 수 없습니다");
        }
    }

    public void checkEmptyTable() {
        if (this.isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다");
        }
    }

    public void updateGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    public void checkNullId() {
        if (this.id == null) {
            throw new IllegalArgumentException("주문 테이블이 존재하지 않습니다");
        }
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
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(tableGroup,
                that.tableGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableGroup, numberOfGuests, empty);
    }
}
