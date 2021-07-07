package kitchenpos.table.domain;

import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;
import kitchenpos.table.dto.OrderTableRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {

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

    @Embedded
    private Orders orders;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long tableGroupId, int numberOfGuests, boolean empty) {
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

    public void groupBy(final Long tableGroupId) {
        this.tableGroupId = tableGroupId;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님의 수는 0보다 작을수 없습니다.");
        }

        if (isEmpty()) {
            throw new IllegalArgumentException("빈테이블은 손님의 수를 변경할수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }

    public void changeEmpty(OrderTableRequest orderTableRequest) {
        if (Objects.nonNull(getTableGroupId())) {
            throw new IllegalArgumentException("단체테이블인 경우 테이블을 비울수 없습니다.");
        }

        this.empty = orderTableRequest.isEmpty();
    }

    public boolean isUnableTableGroup() {
        if (isEmpty() || Objects.nonNull(getTableGroupId())) {
            return true;
        }
        return false;
    }

    public void ungroup() {
        orders.ungroup();
        groupBy(null);
    }
}
