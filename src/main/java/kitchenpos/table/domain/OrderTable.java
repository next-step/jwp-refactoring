package kitchenpos.table.domain;


import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import kitchenpos.common.domain.BaseEntity;
import kitchenpos.exception.CannotUpdatedException;

@Entity
public class OrderTable extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "table_group_id")
    private Long tableGroupId;

    private Long orderId;

    @Enumerated(EnumType.STRING)
    private TableStatus tableStatus = TableStatus.EMPTY;

    @Embedded
    private GuestNumber numberOfGuests;

    protected OrderTable() {
    }

    private OrderTable(Integer numberOfGuests, Boolean empty) {
        this.numberOfGuests = GuestNumber.valueOf(numberOfGuests);
        this.tableStatus = TableStatus.valueOfEmpty(empty);
    }

    public static OrderTable of(Integer numberOfGuests, Boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

//    public void addOrder(Order order) {
//        orders.add(order);
//    }

//    public void removeOrder(Order order) {
//        this.orders.remove(order);
//    }

    public void changeTableStatus(TableStatus tableStatus) {
        this.tableStatus = tableStatus;
    }

    public OrderTable changeEmpty(Boolean empty) {
        validateChangeEmpty();
        this.tableStatus = TableStatus.valueOfEmpty(empty);
        return this;
    }

    public void changeNumberOfGuests(Integer numberOfGuests) {
        validateChangeNumberOfGuests();
        this.numberOfGuests = GuestNumber.valueOf(numberOfGuests);
    }

    public boolean isNotEmptyTableGroup() {
        return Objects.nonNull(tableGroupId);
    }

    public boolean isNotEmpty() {
        return !isEmpty();
    }

    public Long getId() {
        return id;
    }

    public Integer getNumberOfGuests() {
        return numberOfGuests.get();
    }

    public Boolean isEmpty() {
        return TableStatus.EMPTY.equals(this.tableStatus);
    }

    private void validateChangeEmpty() {
        if (Objects.nonNull(tableGroupId)) {
            throw new CannotUpdatedException("단체지정된 테이블은 변경할 수 없습니다.");
        }
        validateOnGoingOrder();
    }

    private void validateChangeNumberOfGuests() {
        if (TableStatus.EMPTY.equals(this.tableStatus)) {
            throw new CannotUpdatedException("빈 테이블의 손님수는 변경 할 수 없습니다.");
        }
    }

    protected void validateOnGoingOrder() {
        if (TableStatus.ORDERED.equals(this.tableStatus)) {
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

    public String getTableStatus() {
        return tableStatus.name();
    }
}
