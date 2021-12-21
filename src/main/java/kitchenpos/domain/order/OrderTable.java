package kitchenpos.domain.order;

import java.security.InvalidParameterException;
import java.util.Objects;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
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

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "table_group_id", foreignKey = @ForeignKey(name = "fk_order_table_table_group"))
    private TableGroup tableGroup;

    @Embedded
    private NumberOfGuests numberOfGuests;

    @Embedded
    private Empty empty;

    protected OrderTable() {
    }

    private OrderTable(int numberOfGuests, boolean empty) {
        this.numberOfGuests = NumberOfGuests.of(numberOfGuests);
        this.empty = Empty.of(empty);
    }

    public static OrderTable of(int numberOfGuests, boolean empty) {
        return new OrderTable(numberOfGuests, empty);
    }

    public void changeNumberOfGuests(int changeNumberOfGuests) {
        empty.validNotEmpty();
        this.numberOfGuests = numberOfGuests.changeNumberOfGuests(changeNumberOfGuests);
    }

    public boolean isEmpty() {
        return empty.isEmpty();
    }

    public void changeEmpty(Orders orders, boolean empty) {
        validTableGroupNotInclude();

        if (!orders.isOrdersAllCompleted()) {
            throw new InvalidParameterException("계산 완료 상태가 아니면 빈테이블 상태를 변경 할 수 없습니다.");
        }

        this.empty.changeEmpty(empty);
    }

    public void changeTableGroup(TableGroup tableGroup) {
        validTableGroupNotInclude();
        empty.validNotEmpty();
        this.tableGroup = tableGroup;
        this.empty.changeEmpty(false);
    }

    public void ungroup(Orders orders) {
        if (orders.isOrdersAllCompleted()) {
            throw new InvalidParameterException("계산 완료 상태가 아니면 단체지정을 해지 할 수 없습니다.");
        }
        tableGroup = null;
    }

    public Long getId() {
        return id;
    }

    public Long getTableGroupId() {
        if (Objects.isNull(tableGroup)) {
            return null;
        }
        return tableGroup.getId();
    }

    public int getNumberOfGuests() {
        return numberOfGuests.value();
    }

    private void validTableGroupNotInclude() {
        if (Objects.nonNull(tableGroup)) {
            throw new InvalidParameterException("단체지정이 되어있는 테이블입니다.");
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
        if (Objects.isNull(id)) {
            return false;
        }

        OrderTable that = (OrderTable) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
