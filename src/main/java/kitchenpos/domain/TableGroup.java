package kitchenpos.domain;

import kitchenpos.dto.OrderTableRequest;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private final List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        group(orderTables);
        this.orderTables.addAll(orderTables);
    }

    public Long getId() {
        return id;
    }

    public void setId(final Long id) {
        this.id = id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(final LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    private void group(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> orderTable.includeInGroup(this));
    }

    public void ungroup() {
        if (orderTables.size() == 0) {
            throw new NoSuchElementException("단체 지정된 주문 테이블이 존재하지 않습니다.");
        }
        orderTables.forEach(OrderTable::excludeFromGroup);
    }

    public static void validateInputOrderTable(List<OrderTableRequest> orderTables) {
        if (Objects.isNull(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "TableGroup{" +
                "id=" + id +
                ", orderTables=" + orderTables +
                '}';
    }
}
