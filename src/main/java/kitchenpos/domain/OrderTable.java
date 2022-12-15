package kitchenpos.domain;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private Orders orders = new Orders();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private OrderTableEmpty empty;


    protected OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = new OrderTableEmpty(empty);
    }

    public void addOrder(Order order) {
        orders.addOrder(order);
    }

    public void changeEmpty(final boolean empty) {
        validateAlreadyTableGroup();
        validateOrderStatus(Arrays.asList(OrderStatus.COOKING.name(), OrderStatus.MEAL.name()));
        this.empty.changeEmpty(empty);
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        validateEmpty();
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public void changeTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void validateOrderStatus(List<String> orderStatuses) {
        orders.validateOrderStatus(orderStatuses);
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getTableGroupId() {
        if (tableGroup == null) {
            return null;
        }
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    @Override
    public String toString() {
        return "OrderTable{" +
                "id=" + id +
                ", numberOfGuests=" + numberOfGuests +
                ", empty=" + empty +
                '}';
    }

    private void validateEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 주문 테이블 입니다[" + this + "]");
        }
    }

    private void validateAlreadyTableGroup() {
        if (Objects.nonNull(getTableGroup())) {
            throw new IllegalArgumentException("이미 단체 지정이 된 주문 테이블입니다[" + this + "]");
        }
    }
}
