package kitchenpos.table.domain;


import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.exception.CannotUpdatedException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;

@Entity
public class OrderTable extends BaseEntity {

    @Embedded
    private final Orders orders = Orders.create();
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Long table_group_id;
    @Embedded
    private GuestNumber numberOfGuests;
    @Embedded
    private EmptyTable empty;

    protected OrderTable() {
    }

    private OrderTable(Integer numberOfGuests, boolean empty) {
        this.numberOfGuests = GuestNumber.valueOf(numberOfGuests);
        this.empty = EmptyTable.valueOf(empty);
    }

    public static OrderTable of(Integer numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public void addOrder(Order order) {
        orders.add(order);
    }

    public void removeOrder(Order order) {
        this.orders.remove(order);
    }

    public OrderTable updateEmpty(Boolean empty) {
        validateUpdateEmpty();
        this.empty = EmptyTable.valueOf(empty);
        return this;
    }

    public void updateNumberOfGuests(Integer numberOfGuests) {
        validateUpdateNumberOfGuests();
        this.numberOfGuests = GuestNumber.valueOf(numberOfGuests);
    }

    public boolean isNotEmptyTableGroup() {
        return Objects.nonNull(table_group_id);
    }

    public boolean isNotEmpty() {
        return empty.equals(EmptyTable.valueOf(Boolean.FALSE));
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.get();
    }

    public Boolean isEmpty() {
        return empty.isEmpty();
    }

    private void validateUpdateEmpty() {
        if (Objects.nonNull(table_group_id)) {
            throw new CannotUpdatedException("단체지정된 테이블은 변경할 수 없습니다.");
        }

        validateOnGoingOrder();
    }

    private void validateUpdateNumberOfGuests() {
        if (empty.isEmpty()) {
            throw new CannotUpdatedException("빈 테이블의 손님수는 변경 할 수 없습니다.");
        }
    }

    public void validateOnGoingOrder() {
        if (orders.isOnGoing()) {
            throw new CannotUpdatedException("주문이 완료되지 않은 테이블이 있습니다.");
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
        OrderTable that = (OrderTable) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

}
