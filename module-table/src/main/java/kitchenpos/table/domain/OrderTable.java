package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.table.exception.CannotChangeNumberOfGuestException;
import kitchenpos.table.exception.CannotChangeTableEmptyException;

@Entity
public class OrderTable {

    public static final String THIS_IS_A_GROUP_ORDER_TABLE = "단체 지정된 주문테이블인 경우 빈 테이블로 변경이 불가능합니다.";
    public static final String THE_NUMBER_OF_GUESTS_CANNOT_BE_NEGATIVE = "변경하려는 손님 수는 음수일 수 없다.";
    public static final String THERE_IS_AN_EMPTY_ORDER_TABLE = "빈 테이블의 주문 테이블은 손님 수를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    @Column(nullable = false)
    private int numberOfGuests;

    @Column(nullable = false)
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public OrderTable(Long id, int numberOfGuests) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = true;
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, Long tableGroupId, int numberOfGuests) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = true;
    }

    public OrderTable(Long id, Long tableGroupId, Integer numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
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

    public void ungroup() {
        this.tableGroupId = null;
    }

    public void changeEmpty(boolean empty) {
        if (empty) {
            validationChangeEmpty();
        }
        this.empty = empty;
    }

    private void validationChangeEmpty() {
        if (Objects.nonNull(tableGroupId)) {
            throw new CannotChangeTableEmptyException(THIS_IS_A_GROUP_ORDER_TABLE);
        }
    }

    public void changeTableGroupId(long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        validationChangeNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void validationChangeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new CannotChangeNumberOfGuestException(THE_NUMBER_OF_GUESTS_CANNOT_BE_NEGATIVE);
        }
        if (empty) {
            throw new CannotChangeNumberOfGuestException(THERE_IS_AN_EMPTY_ORDER_TABLE);
        }
    }
}
