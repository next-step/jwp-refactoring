package kitchenpos.ordertable.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.ordertable.exception.TableChangeNumberOfGuestsException;
import kitchenpos.ordertable.exception.TableUpdateStateException;
import kitchenpos.ordertable.vo.NumberOfGuests;

@Entity
public class OrderTable {

    private static final String ERROR_MESSAGE_TABLE_IN_GROUP = "테이블 그룹에 속해있는 테이블은 상태를 변경할 수 없습니다.";
    private static final String ERROR_MESSAGE_CANNOT_CHANGE_NUM_OF_GUESTS_WHEN_ORDER_CLOSED = "주문 종료 상태에선 방문 손님 수를 변경할 수 없습니다.";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long tableGroupId;

    @Embedded
    @Column(nullable = false)
    private NumberOfGuests numberOfGuests;

    @Column(nullable = false)
    private boolean orderClose = false;

    protected OrderTable() {
    }

    public OrderTable(Long id) {
        this.id = id;
    }

    public OrderTable(boolean orderClose) {
        this.orderClose = orderClose;
    }

    public OrderTable(NumberOfGuests numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public OrderTable(NumberOfGuests numberOfGuests, boolean orderClose) {
        this(null, null, numberOfGuests, orderClose);
    }

    public OrderTable(Long id, NumberOfGuests numberOfGuests, boolean orderClose) {
        this(id, null, numberOfGuests, orderClose);
    }

    public OrderTable(Long id, Long tableGroupId, NumberOfGuests numberOfGuests,
        boolean orderClose) {
        this.id = id;
        this.tableGroupId = tableGroupId;
        this.numberOfGuests = numberOfGuests;
        this.orderClose = orderClose;
    }

    public void updateTableStatus(boolean orderClose) {
        if (Objects.nonNull(tableGroupId)) {
            throw new TableUpdateStateException(ERROR_MESSAGE_TABLE_IN_GROUP);
        }
        this.orderClose = orderClose;
    }

    public void changeNumberOfGuests(NumberOfGuests numberOfGuests) {
        if (isOrderClose()) {
            throw new TableChangeNumberOfGuestsException(
                ERROR_MESSAGE_CANNOT_CHANGE_NUM_OF_GUESTS_WHEN_ORDER_CLOSED);
        }
        this.numberOfGuests = numberOfGuests;
    }

    public void groupIn(Long tableGroupId) {
        this.tableGroupId = tableGroupId;
        changeOrderClose(false);
    }

    public boolean hasGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public void unGroup() {
        this.tableGroupId = null;
    }

    public void changeOrderClose(final boolean orderClose) {
        this.orderClose = orderClose;
    }

    public boolean isOrderClose() {
        return orderClose;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        return tableGroupId;
    }

    public NumberOfGuests getNumberOfGuests() {
        return numberOfGuests;
    }

    public int getNumberOfGuestsVal() {
        return numberOfGuests.getNumberOfGuests();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }

        if (!(o instanceof OrderTable)) {
            return false;
        }

        OrderTable that = (OrderTable) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

}
