package table.domain;

import java.util.*;

import javax.persistence.*;

import common.*;

@Entity
public class OrderTable {
    private static final String TABLE_GROUP_IS_EXIST_EXCEPTION_STATEMENT = "그룹 테이블이 존재합니다.";
    private static final String ORDER_TABLE_IS_NOT_EMPTY_EXCEPTION_STATEMENT = "주문 테이블이 비어있지 않습니다.";
    private static final String TABLE_GROUP_IS_NOT_EXIST_EXCEPTION_STATEMENT = "그룹 테이블이 존재하지 않습니다.";
    private static final String NUMBER_OF_GUESTS = "방문한 손님 수";
    private static final int NUMBER_OF_GUESTS_MIN = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        validate(numberOfGuests);
        this.tableGroup = null;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, numberOfGuests, empty);
    }

    public OrderTable(TableGroup tableGroup, int numberOfGuests, boolean empty) {
        validate(numberOfGuests);
        this.tableGroup = tableGroup;
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

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeTableGroup(TableGroup tableGroup) {
        if (Objects.nonNull(this.tableGroup)) {
            throw new IllegalArgumentException(TABLE_GROUP_IS_EXIST_EXCEPTION_STATEMENT);
        }

        if (!this.empty) {
            throw new IllegalArgumentException(ORDER_TABLE_IS_NOT_EMPTY_EXCEPTION_STATEMENT);
        }

        this.tableGroup = tableGroup;
        this.empty = false;
    }

    public void initTableGroup() {
        if (Objects.isNull(this.tableGroup)) {
            throw new IllegalArgumentException(TABLE_GROUP_IS_NOT_EXIST_EXCEPTION_STATEMENT);
        }

        this.tableGroup = null;
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

}