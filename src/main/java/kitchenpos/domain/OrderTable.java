package kitchenpos.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import kitchenpos.dto.OrderTableRequest;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "table_group_id")
    private TableGroup tableGroup;

    private int numberOfGuests;

    private boolean empty;

    @OneToMany(mappedBy = "orderTable", fetch = FetchType.LAZY, cascade = CascadeType.PERSIST)
    @JsonIgnoreProperties(value = {"orderTable"}, allowSetters = true)
    private List<Order> orders = new ArrayList<>();

    public OrderTable() {
    }

    public OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public OrderTable(Long id, int numberOfGuests, boolean empty) {
        this.id = id;
        this.numberOfGuests = numberOfGuests;
        this.empty = empty;
    }

    public static OrderTable from(OrderTableRequest orderTableRequest) {
        return new OrderTable(orderTableRequest.getNumberOfGuests(), orderTableRequest.isEmpty());
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

    public TableGroup getTableGroup() {
        return tableGroup;
    }

    public List<Order> getOrders() {
        return orders;
    }

    public void changeEmpty(boolean empty) {
        checkGroupedOrderTable();
        getOrders()
                .forEach(Order::checkOrderStatusCookingOrMeal);

        this.empty = empty;
    }

    public void changeNonEmptyOrderTable() {
        this.empty = false;
    }

    public void changeNumberOfGuests(int numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
        checkNumberOfGuestsOverZero();
    }

    public void addTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public void checkAvailable() {
        if (!this.isEmpty() || Objects.nonNull(this.getTableGroup())) {
            throw new IllegalArgumentException("주문테이블이 비어있지 않거나, 단체지정이 되어있습니다.");
        }
    }

    public void checkIsEmpty() {
        if (isEmpty()) {
            throw new IllegalArgumentException("주문 테이블이 비어있습니다.");
        }
    }

    public void checkNumberOfGuestsOverZero() {
        if (numberOfGuests < 0) {
            throw new IllegalArgumentException("주문 테이블 손님 수는 0 명 이상으로 입력해야 합니다.");
        }
    }

    public void checkGroupedOrderTable() {
        if (Objects.nonNull(getTableGroup())) {
            throw new IllegalArgumentException("이미 단체지정으로 등록되어 있는 주문 테이블입니다.");
        }
    }

    public void unGroup() {
        orders.forEach(Order::checkOrderStatusCookingOrMeal);
        this.tableGroup = null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        OrderTable that = (OrderTable) o;
        return numberOfGuests == that.numberOfGuests && empty == that.empty && Objects.equals(id, that.id) && Objects.equals(tableGroup, that.tableGroup);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, tableGroup, numberOfGuests, empty);
    }


}
