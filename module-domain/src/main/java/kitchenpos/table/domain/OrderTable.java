package kitchenpos.table.domain;

import java.util.*;

import javax.persistence.*;

import com.fasterxml.jackson.annotation.*;

import kitchenpos.common.*;

@Entity
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderTable {
    private static final String TABLE_GROUP_IS_EXIST_EXCEPTION_STATEMENT = "그룹 테이블이 존재합니다.";
    private static final String ORDER_TABLE_IS_NOT_EMPTY_EXCEPTION_STATEMENT = "주문 테이블이 비어있지 않습니다.";
    private static final String TABLE_GROUP_IS_NOT_EXIST_EXCEPTION_STATEMENT = "그룹 테이블이 존재하지 않습니다.";
    private static final String NUMBER_OF_GUESTS = "방문한 손님 수";
    private static final int NUMBER_OF_GUESTS_MIN = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        validate(numberOfGuests);
        this.tableGroupId = null;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
        validate(numberOfGuests);
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    private void validate(int numberOfGuests) {
        if (numberOfGuests < NUMBER_OF_GUESTS_MIN) {
            throw new WrongValueException(NUMBER_OF_GUESTS);
        }
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeTableGroup(Long tableGroupId) {
        if (Objects.nonNull(this.tableGroupId)) {
            throw new IllegalArgumentException(TABLE_GROUP_IS_EXIST_EXCEPTION_STATEMENT);
        }

        if (!this.empty) {
            throw new IllegalArgumentException(ORDER_TABLE_IS_NOT_EMPTY_EXCEPTION_STATEMENT);
        }

        this.tableGroupId = tableGroupId;
        this.empty = false;
    }

    public void initTableGroup() {
        if (Objects.isNull(this.tableGroupId)) {
            throw new IllegalArgumentException(TABLE_GROUP_IS_NOT_EXIST_EXCEPTION_STATEMENT);
        }

        this.tableGroupId = null;
    }

    public void cleanTable() {
        this.empty = true;
        this.numberOfGuests = 0;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < NUMBER_OF_GUESTS_MIN) {
            throw new WrongValueException(NUMBER_OF_GUESTS);
        }
        this.numberOfGuests = numberOfGuests;
        this.empty = false;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(tableGroupId, that.tableGroupId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tableGroupId, numberOfGuests, empty);
    }
}
