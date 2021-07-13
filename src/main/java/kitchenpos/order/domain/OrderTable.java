package kitchenpos.order.domain;

import kitchenpos.common.NumberOfGuests;

import javax.persistence.*;
import java.util.*;
import java.util.Objects;

@Entity
public class OrderTable {
    private static final Long TABLE_GROUP_NULL = -1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    @OneToMany(mappedBy = "orderTable")
    private List<Order> orders = new ArrayList<>();

    private NumberOfGuests numberOfGuests;
    private boolean empty;

    public OrderTable() {}

    public OrderTable(int numberOfGuests, boolean empty) {
        this.tableGroup = null;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public OrderTable(Long id, TableGroup tableGroup, int numberOfGuests, boolean empty) {
        this.id = id;
        this.tableGroup = tableGroup;
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
        this.empty = empty;
    }

    public Long getId() {
        return id;
    }

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroup)) {
            return TABLE_GROUP_NULL;
        }
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests.number();
    }

    public void updateNumberOfGuests(final int numberOfGuests) {
        if (getEmpty()) {
            throw new IllegalArgumentException("비어있는 테이블은 인원수 변경이 불가합니다.");
        }
        this.numberOfGuests = new NumberOfGuests(numberOfGuests);
    }

    public boolean getEmpty() {
        return empty;
    }

    public void updateEmpty(final boolean empty, final List<Order> orders) {
        validGroupTableNull("그룹으로 묶여있는 경우 테이블을 비을 수 없습니다.");
        validOrderStatusCompletion(orders);
        this.empty = empty;
    }

    public void validGroupOrderTable() {
        validEmpty();
        validGroupTableNull("이미 그룹으로 묶여있는 경우 그룹화 할 수 없습니다.");
    }

    public void unGroup() {
        this.tableGroup = null;
    }

    private void validEmpty() {
        if(!getEmpty()) {
            throw new IllegalArgumentException("비어있지 않은 테이블은 그룹화 할 수 없습니다.");
        }
    }

    private void validGroupTableNull(String errorMessage) {
        if (getTableGroupId() != TABLE_GROUP_NULL) {
            throw new IllegalArgumentException(errorMessage);
        }
    }

    public void validOrderStatusCompletion(List<Order> orders){
        if (!isOrderStatusCompletion(orders)) {
            throw new IllegalArgumentException("주문 테이블의 주문 상태가 조리 또는 식사인 경우 테이블을 비울 수 없습니다.");
        }
    }
    public boolean isOrderStatusCompletion(List<Order> orders) {
        return orders.stream()
                .filter(order -> order.isOrderStatusCompletion())
                .findFirst()
                .isPresent();
    }
}
