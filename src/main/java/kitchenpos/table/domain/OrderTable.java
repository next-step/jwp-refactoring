package kitchenpos.table.domain;

import static java.util.Objects.*;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

import kitchenpos.domain.Order;
import kitchenpos.tablegroup.domain.TableGroup;

@Entity
public class OrderTable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    private boolean empty;

    protected OrderTable() {}

    public OrderTable(int numberOfGuests, boolean empty) {
        this(numberOfGuests, empty, new ArrayList<>());
    }

    public OrderTable(int numberOfGuests, boolean empty, List<Order> orders) {
        this.numberOfGuests = NumberOfGuests.valueOf(numberOfGuests);
        this.empty = empty;
        this.orders = orders;
    }

    public Long getId() {
        return id;
    }

    public void ungrouped() {
        validateCompletedOrders("조리상태이거나 식사상태인 주문이 있는 주문테이블은 그룹해제를 할 수 없습니다.");
        this.tableGroup = null;
    }

    public int getNumberOfGuests() {
        return numberOfGuests.getNumberOfGuests();
    }

    public void changeNumberOfGuests(final int numberOfGuests) {
        if (isEmpty()) {
            throw new IllegalArgumentException("빈 테이블은 방문 손님 수를 수정할 수 없습니다.");
        }
        this.numberOfGuests = NumberOfGuests.valueOf(numberOfGuests);
    }

    public boolean isEmpty() {
        return empty;
    }

    public boolean isNotEmpty() {
        return !empty;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public void updateEmpty(boolean isEmpty) {
        validate();
        this.empty = isEmpty;
    }

    public boolean isGrouped() {
        return nonNull(tableGroup);
    }

    void toGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
        emptyOff();
    }

    private void emptyOff() {
        this.empty = false;
    }

    private void validate() {
        validateNotGrouped();
        validateCompletedOrders("조리상태이거나 식사상태주문의 주문테이블은 상태를 변경할 수 없습니다.");
    }

    private void validateNotGrouped() {
        if (isGrouped()) {
            throw new IllegalArgumentException("그룹 설정이 되어 있는 테이블은 주문 등록 불가 상태로 바꿀 수 없습니다.");
        }
    }

    private void validateCompletedOrders(String errorMessage) {
        boolean isCompleteAll = orders.stream().allMatch(Order::isComplete);
        if (!isCompleteAll) {
            throw new IllegalArgumentException(errorMessage);
        }
    }
}
