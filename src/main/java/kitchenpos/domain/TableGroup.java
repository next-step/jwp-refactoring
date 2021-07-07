package kitchenpos.domain;

import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    private LocalDateTime createdDate;
    @OneToMany(mappedBy = "tableGroup")
    private List<OrderTable> orderTables;

    protected TableGroup() {}

    public TableGroup(Long id, LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroup(List<OrderTable> orderTables) {
        verifyAvailable(orderTables);
        group(orderTables);
    }

    public void group(List<OrderTable> orderTables) {
        orderTables.stream()
                .forEach(orderTable -> {
                    orderTable.changeTableGroup(this);
                    orderTable.setEmpty(false);
                });
        this.orderTables = orderTables;
    }

    public void ungroup() {
        orderTables.stream()
                .forEach(orderTable -> orderTable.changeTableGroup(null));
    }

    private void verifyAvailable(List<OrderTable> orderTables) {
        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException();
        }

        if (orderTables.stream()
                .filter(orderTable -> !orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup()))
                .findFirst()
                .isPresent()) {
            throw new IllegalArgumentException();
        }
    }

    public TableGroup(LocalDateTime createdDate, List<OrderTable> orderTables) {
        this.createdDate = createdDate;
        this.orderTables = orderTables;
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

    public void setOrderTables(final List<OrderTable> orderTables) {
        this.orderTables = orderTables;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(getId(), that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }
}
