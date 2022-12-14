package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.exception.InvalidParameterException;

@Entity
public class OrderTable {
    private static final String ERROR_MESSAGE_ALREADY_EXIST_TABLE_GROUP = "단체 테이블이 존재합니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "table_group_id")
    private Long tableGroupId;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Embedded
    private Empty empty;

    protected OrderTable() {}

    private OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = Empty.from(empty);
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable of(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        return new OrderTable(id, tableGroupId, numberOfGuests, empty);
    }

    public boolean isEmpty() {
        return empty.isTrue();
    }

    public void isGrouped() {
        if (tableGroupId != null) {
            throw new InvalidParameterException(ERROR_MESSAGE_ALREADY_EXIST_TABLE_GROUP);
        }
    }

    public void changeEmpty() {
        this.tableGroupId = null;
        this.numberOfGuests = NumberOfGuests.from(0);
        this.empty = Empty.from(true);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = NumberOfGuests.from(numberOfGuests);
        this.empty = Empty.from(false);
    }

    public void updateTableGroup(long tableGroupId) {
        this.tableGroupId = tableGroupId;
        this.empty = Empty.from(false);
    }

    public void ungroup() {
        this.tableGroupId = null;
    }

    public Long id() {
        return id;
    }

    public Long tableGroupId() {
        return tableGroupId;
    }

    public int numberOfGuests() {
        return numberOfGuests.value();
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
