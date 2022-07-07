package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Embedded
    private NumberOfGuests numberOfGuests;

    private boolean empty;

    private OrderTable(OrderTable orderTable) {
        this(orderTable.id, orderTable.tableGroupId, orderTable.numberOfGuests, orderTable.empty);
    }

    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, int numberOfGuests) {
        this(id, numberOfGuests, false);
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(id, null, numberOfGuests, empty);
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests, boolean empty) {
        this(id, tableGroupId, NumberOfGuests.from(numberOfGuests), empty);
    }

    public OrderTable(Long id, Long tableGroupId, NumberOfGuests numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable from(OrderTable orderTable) {
        return new OrderTable(orderTable);
    }

    public void changeEmpty(boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalArgumentException("단체 지정된 테이블은 빈 테이블 상태를 변경할 수 없습니다.");
        }
        this.empty = empty;
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블의 손님 수를 변경할 수 없습니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void setGroup(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void resetGroup() {
        this.tableGroupId = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    public boolean isEmpty() {
        return empty;
    }
}
