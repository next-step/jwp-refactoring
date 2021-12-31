package kitchenpos.core.domain;

import kitchenpos.core.exception.CanNotChangeOrderTableException;
import kitchenpos.core.validator.OrderTableChangeEmptyValidator;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.Objects;

@Entity
public class OrderTable {
    private static final String CAN_NOT_CHANGE_NUMBER_OF_GUESTS = "빈 테이블일 경우 방문한 손님 수를 변경할 수 없습니다.";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Embedded
    private NumberOfGuests numberOfGuests;
    @Embedded
    private OrderTableEmpty empty;
    @Column(name = "table_group_id")
    private Long tableGroupId;

    public OrderTable() {
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = OrderTableEmpty.of(empty);
    }

    private OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty);
        this.id = id;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public static OrderTable generate(Long id, int numberOfGuests, boolean empty) {
        return new OrderTable(id, numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public boolean matchNumberOfGuests(int targetNumberOfGuests) {
        return numberOfGuests.matchNumberOfGuests(targetNumberOfGuests);
    }

    public void changeNumberOfGuests(int targetNumberOfGuests) {
        if (isEmpty()) {
            throw new CanNotChangeOrderTableException(CAN_NOT_CHANGE_NUMBER_OF_GUESTS);
        }
        this.numberOfGuests = NumberOfGuests.of(targetNumberOfGuests);
    }

    public void changeEmpty(boolean empty, OrderTableChangeEmptyValidator orderTableChangeEmptyValidator) {
        orderTableChangeEmptyValidator.validate(id);
        this.empty = OrderTableEmpty.of(empty);
    }

    public void ungroup() {
        tableGroupId = null;
    }

    public void group(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    @Override
    public boolean equals(Object target) {
        if (this == target) return true;
        if (target == null || getClass() != target.getClass()) return false;

        OrderTable that = (OrderTable) target;

        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }
}
