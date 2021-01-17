package kitchenpos.ordertable.domain;

import javax.persistence.*;
import java.util.Objects;

@Entity
public class OrderTable {

    private static final int MIN_NUMBER_OF_GUESTS = 0;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(name = "number_of_guests")
    private int numberOfGuests;

    @Column(name = "empty")
    private boolean empty;

    protected OrderTable() {
    }

    public OrderTable(final int numberOfGuests, final boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(final Long tableGroupId, final int numberOfGuests, final boolean empty) {
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public void changeEmpty(final boolean empty) {
        if (Objects.nonNull(tableGroupId)) {
            throw new IllegalStateException("단체 지정이 되어 있다면 등록 상태를 변경할 수 없습니다.");
        }

        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (empty) {
            throw new IllegalStateException("빈 테이블은 손님 수를 변경할 수 없습니다.");
        }

        if (numberOfGuests < MIN_NUMBER_OF_GUESTS) {
            throw new IllegalArgumentException("손님 수는 0명 이상이어야 합니다.");
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void assign(final Long tableGroupId) {
        changeEmpty(false);
        this.tableGroupId = tableGroupId;
    }

    public void ungroup() {
        if (Objects.isNull(tableGroupId)) {
            throw new IllegalStateException("단체 지정되어 있지 않은 테이블은 해제할 수 없습니다.");
        }
        this.tableGroupId = null;
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
}
