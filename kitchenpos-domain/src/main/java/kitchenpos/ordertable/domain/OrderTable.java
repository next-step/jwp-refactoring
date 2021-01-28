package kitchenpos.ordertable.domain;

import kitchenpos.ordertablegroup.domain.OrderTableGroup;

import javax.persistence.*;
import java.util.Objects;

@Entity(name = "order_table")
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int numberOfGuests;
    private boolean empty;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "table_group_id")
    private OrderTableGroup orderTableGroup;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(OrderTableGroup orderTableGroup, int numberOfGuests, boolean empty) {
        this.orderTableGroup = orderTableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public OrderTableGroup getOrderTableGroup() {
        return orderTableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public Long getTableGroupId() {
        return orderTableGroup == null ? null : orderTableGroup.getId();
    }

    public void setOrderTableGroup(OrderTableGroup orderTableGroup) {
        this.orderTableGroup = orderTableGroup;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(final boolean empty, boolean isIncludeNotCompleteStatus) {
        validateOrdersStatus(isIncludeNotCompleteStatus);
        this.empty = empty;
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void ungroupTable(boolean isIncludeNotCompleteStatus) {
        validateOrdersStatus(isIncludeNotCompleteStatus);
        this.orderTableGroup = null;
    }

    public boolean isNotAvailableOrderTable() {
        return Objects.nonNull(this.getOrderTableGroup());
    }

    private void validateNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("게스트 인원이 0보다 커야한다.");
        }
    }


    private void validateOrdersStatus(boolean isIncludeNotCompleteStatus) {
        if (isIncludeNotCompleteStatus) {
            throw new IllegalArgumentException("`조리중`과 `식사중`에는 변경할 수 없다.");
        }
    }

}
