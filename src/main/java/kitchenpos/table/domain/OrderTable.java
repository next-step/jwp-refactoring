package kitchenpos.table.domain;

import kitchenpos.common.domain.BaseEntity;
import kitchenpos.common.exception.BadRequestException;
import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.Orders;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    @Embedded
    private Orders orders = new Orders();

    protected OrderTable() {}

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        checkNumberOfGuestsValidation(numberOfGuests);
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(null, null, numberOfGuests, empty);
    }

    public void addOrder(Order order) {
        orders.addOrder(order);
    }

    public void changeEmpty(boolean empty) {
        checkChangeEmptyValidation();
        this.empty = empty;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        checkNumberOfGuestsValidation(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    public void addGroup(TableGroup tableGroup) {
        checkAddableGroup();
        changeEmpty(false);
        this.tableGroup = tableGroup;
    }

    public void removeGroup() {
        checkRemovableOrder();
        tableGroup = null;
    }

    private void checkAddableGroup() {
        if (isTableGrouping()) {
            throw new BadRequestException("이미 그룹화된 주문 테이블 입니다.");
        }

        if (!isEmpty()) {
            throw new BadRequestException("빈 테이블이 아닌 주문 테이블은 그룹화할 수 없습니다.");
        }
    }

    private boolean isTableGrouping() {
        return !Objects.isNull(tableGroup);
    }

    private void checkRemovableOrder() {
        if (!orders.isCompleteOrders()) {
            throw new BadRequestException("주문이 완료 되지 않아 그룹을 해제할 수 없습니다.");
        }
    }

    private void checkChangeEmptyValidation() {
        if (isTableGrouping()) {
            throw new BadRequestException("테이블 그룹이 존재하므로 빈 테이블 설정을 할 수 없습니다.");
        }

        if (!orders.isCompleteOrders()) {
            throw new BadRequestException("현재 테이블은 주문 완료 상태가 아니므로 빈 테이블 설정을 할 수 없습니다.");
        }
    }

    private void checkNumberOfGuestsValidation(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("손님 수 변경 요청은 0명 이상 가능합니다.");
        }

        if (isEmpty()) {
            throw new BadRequestException("빈 테이블의 손님 수를 설정할 수 없습니다.");
        }
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }



    public List<Order> getOrders() {
        return orders.asList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
