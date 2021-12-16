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
import kitchenpos.common.exception.InvalidStatusException;
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
    private CustomerStatus status;

    protected OrderTable() {
    }

    private OrderTable(Headcount numberOfGuests, CustomerStatus status) {
        setNumberOfGuests(numberOfGuests);
        setStatus(status);
    }

    public static OrderTable empty(Headcount numberOfGuests) {
        return new OrderTable(numberOfGuests, CustomerStatus.EMPTY);
    }

    public static OrderTable place(Headcount numberOfGuests) {
        return new OrderTable(numberOfGuests, CustomerStatus.PLACE);
    }

    public long id() {
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

    public void changeNumberOfGuests(Headcount numberOfGuests) {
        if (status.isEmpty()) {
            throw new InvalidStatusException(
                String.format("비어있는 주문 테이블(%s)의 방문한 손님 수를 변경할 수 없습니다.", this));
        }
        setNumberOfGuests(numberOfGuests);
    }

    public boolean isEmpty() {
        return status.isEmpty();
    }

    public void changeStatus(CustomerStatus status) {
        validateStatus();
        this.status = status;
    }

    public boolean isCookingOrMeal() {
        return status.isOrdered();
    }

    boolean notHaveGroupAndEmpty() {
        return notHaveGroup() && status.isEmpty();
    }

    void changeGroup(TableGroup tableGroup) {
        this.status = CustomerStatus.PLACE;
        this.tableGroup = tableGroup;
    }

    void ungroup() {
        tableGroup = null;
    }

    private void setNumberOfGuests(Headcount numberOfGuests) {
        Assert.notNull(numberOfGuests, "방문한 손님 수는 필수입니다.");
        this.numberOfGuests = numberOfGuests;
    }

    private void setStatus(CustomerStatus status) {
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
