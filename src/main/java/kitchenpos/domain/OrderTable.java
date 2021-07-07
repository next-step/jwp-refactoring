package kitchenpos.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.exception.OrderNotCompletedException;

@Entity
public class OrderTable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private TableGroup tableGroup;

    private int numberOfGuests;
    private boolean empty;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this(null, numberOfGuests, empty);
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this(id,null, numberOfGuests, empty);
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public int getNumberOfGuests() {
        return numberOfGuests;
    }

    public boolean isEmpty() {
        return empty;
    }

    public void changeEmpty(boolean empty) {
        checkTableGroup();
        orders.forEach(this::checkOrderStatus);
        this.empty = empty;
    }

    private void checkTableGroup() {
        if (Objects.nonNull(tableGroup)) {
            throw new IllegalArgumentException("테이블 그룹에 포함되어 있습니다.");
        }
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        checkTableIsEmpty();
        checkNumberOfGuests(numberOfGuests);
        this.numberOfGuests = numberOfGuests;
    }

    private void checkTableIsEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블의 손님 수를 변경할 수 없습니다.");
        }
    }

    private void checkNumberOfGuests(int numberOfGuests) {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("테이블의 손님 수는 음수가 될 수 없습니다.");
        }
    }

    public boolean hasTableGroup() {
        return Objects.nonNull(tableGroup);
    }

    public void setTableGroup(TableGroup tableGroup) {
        checkTableGroup();
        this.tableGroup = tableGroup;
    }

    public void ungroup() {
        orders.forEach(this::checkOrderStatus);
        this.tableGroup = null;
    }

    public void checkOrderStatus(Order order) {
        if (Objects.nonNull(order) && !order.isCompleted()) {
            throw new OrderNotCompletedException("테이블에 완결되지 않은 주문이 존재합니다.");
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        OrderTable that = (OrderTable)o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    // TODO 이하 삭제

    public Long getTableGroupId() {
        // return tableGroup.getId();
        return 2L; // TODO 흠..
    }

    public void setTableGroupId(Long tableGroupId) {
        // tableGroup.setId(tableGroupId);
        // TODO 흠..
    }

    public void setEmpty(boolean b) {
        this.empty = b;
    }

    public void setId(long aLong) {
        this.id = aLong;
    }

    public void setNumberOfGuests(int number_of_guests) {
        this.numberOfGuests = number_of_guests;
    }

    public void addOrder(Order order) {
        orders.add(order);
    }
}
