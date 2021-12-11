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

    public OrderTable() {
    }

    private OrderTable(Headcount numberOfGuests, TableStatus status) {
        Assert.notNull(numberOfGuests, "방문한 손님 수는 필수입니다.");
        Assert.notNull(status, "테이블 상태는 필수입니다.");
        this.numberOfGuests = numberOfGuests;
        this.status = status;
    }

    public static OrderTable of(Headcount numberOfGuests, TableStatus status) {
        return new OrderTable(numberOfGuests, status);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public boolean hasTableGroup() {
        return tableGroup == null;
    }

    public long getTableGroupId() {
        return tableGroup.getId();
    }

    public void setTableGroup(TableGroup tableGroup) {
        this.tableGroup = tableGroup;
    }

    public Headcount getNumberOfGuests() {
        return numberOfGuests;
    }

    public void setNumberOfGuests(Headcount numberOfGuests) {
        this.numberOfGuests = numberOfGuests;
    }

    public boolean isEmpty() {
        return status.isEmpty();
    }

    public void changeEmpty() {
        this.status = TableStatus.EMPTY;
    }

    public void setStatus(TableStatus status) {
        this.status = status;
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
