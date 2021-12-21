package kitchenpos.domain.order;

import java.security.InvalidParameterException;
import java.util.List;
import java.util.Objects;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    public static final OrderTable EMPTY_TABLE = new EmptyOrderTable();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        valid(numberOfGuests, empty);
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }


    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return tableGroup.getId();
    }

    public void setTableGroupId(final Long tableGroupId) {
        this.tableGroup.setId(tableGroupId);
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(final int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void setEmpty(final boolean empty) {
        this.empty = empty;
    }

    public void changeEmpty(List<Order> orders, boolean empty) {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("단체지정이 되어있는 테이블은 빈테이블 상태를 변경 할 수 없습니다.");
        }

        if (!isOrdersAllCompleted(orders)) {
            throw new IllegalArgumentException("계산 완료 상태가 아니면 빈테이블 상태를 변경 할 수 없습니다.");
        }

        this.empty = empty;
    }

    private boolean isOrdersAllCompleted(List<Order> orders) {
        return orders.stream()
            .allMatch(Order::isComplete);
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("0명 이하의 손님을 설정 할 수 없습니다.");
        }

        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블이면 손님을 받을 수 없습니다.");
        }

        this.numberOfGuests = numberOfGuests;
    }


    private void valid(int numberOfGuests, boolean empty) {
        if (empty && numberOfGuests > 0) {
            throw new IllegalArgumentException("빈 테이블일 경우 손님을 설정 할 수 없습니다.");
        }
    }

    public void validNotIncludeTableGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new InvalidParameterException("단체 지정이 되어 있는 테이블입니다.");
        }
    }

    public void validNotEmpty() {
        if (!isEmpty()) {
            throw new InvalidParameterException("빈 테이블이 아닙니다.");
        }
    }

    public void changeTableGroup(TableGroup tableGroup) {
        validNotIncludeTableGroup();
        validNotEmpty();
        this.tableGroup = tableGroup;
        this.empty = true;
    }

    public void ungroup(List<Order> orders) {
        if (!isOrdersAllCompleted(orders)) {
            throw new IllegalArgumentException("계산 완료 상태가 아니면 단체지정을 해지 할 수 없습니다.");
        }
        tableGroup = null;
    }


    private static class EmptyOrderTable extends OrderTable {

        @Override
        public boolean isEmpty() {
            return true;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        if (Objects.isNull(id)) {
            return false;
        }

        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
