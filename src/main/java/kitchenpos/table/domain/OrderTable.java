package kitchenpos.table.domain;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import kitchenpos.common.exception.InvalidStatusException;
import kitchenpos.order.domain.Order;
import org.springframework.util.Assert;

@Entity
public class OrderTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private Headcount numberOfGuests;

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private TableStatus status;

    @OneToOne(mappedBy = "orderTable")
    private Order order;

    protected OrderTable() {
    }

    private OrderTable(Headcount numberOfGuests, TableStatus status) {
        setNumberOfGuests(numberOfGuests);
        setStatus(status);
    }

    public static OrderTable of(Headcount numberOfGuests, TableStatus status) {
        return new OrderTable(numberOfGuests, status);
    }

    public Long id() {
        return id;
    }

    public boolean hasTableGroup() {
        return !notHaveGroup();
    }

    public TableGroup tableGroup() {
        return tableGroup;
    }

    public Headcount numberOfGuests() {
        return numberOfGuests;
    }

    public void changeOrder(Order order) {
        this.order = order;
    }

    public void changeNumberOfGuests(Headcount numberOfGuests) {
        if (status.isEmpty()) {
            throw new InvalidStatusException(
                String.format("비어있는 주문 테이블(%s)의 방문한 손님 수를 변경할 수 없습니다.", this));
        }
        setNumberOfGuests(numberOfGuests);
    }

    public boolean isFull() {
        return status.isFull();
    }

    public boolean isEmpty() {
        return status.isEmpty();
    }

    public void changeStatus(TableStatus status) {
        validateStatus();
        this.status = status;
    }

    public boolean isCookingOrMeal() {
        if (status.isEmpty() || notExistOrder()) {
            return false;
        }
        return order.isCookingOrMeal();
    }

    boolean notHaveGroupAndEmpty() {
        return notHaveGroup() && status.isEmpty();
    }

    void changeGroup(TableGroup tableGroup) {
        this.status = TableStatus.FULL;
        this.tableGroup = tableGroup;
    }

    void ungroup() {
        tableGroup = null;
    }

    private boolean notExistOrder() {
        return order == null;
    }

    private void setNumberOfGuests(Headcount numberOfGuests) {
        Assert.notNull(numberOfGuests, "방문한 손님 수는 필수입니다.");
        this.numberOfGuests = numberOfGuests;
    }

    private void setStatus(TableStatus status) {
        Assert.notNull(status, "테이블 상태는 필수입니다.");
        this.status = status;
    }

    private boolean notHaveGroup() {
        return tableGroup == null;
    }

    private void validateStatus() {
        if (hasTableGroup()) {
            throw new InvalidStatusException(
                String.format("주문 테이블(%s)은 그룹이 지정되어 있어서 상태를 변경할 수 없습니다.", this));
        }
        if (isCookingOrMeal()) {
            throw new InvalidStatusException(
                String.format("조리 중 또는 식사 중인 주문 테이블(%s)의 상태를 변경할 수 없습니다.", this));
        }
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
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
        return id == that.id;
    }

    @Override
    public String toString() {
        return "OrderTable{" +
            "id=" + id +
            ", numberOfGuests=" + numberOfGuests +
            ", status=" + status +
            '}';
    }
}
