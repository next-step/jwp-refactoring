package kitchenpos.domain;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDateTime createdDate = LocalDateTime.now();

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.EAGER, cascade = {CascadeType.PERSIST, CascadeType.MERGE}, orphanRemoval = true)
    private List<OrderTable> orderTables = new ArrayList<>();

    public TableGroup() {
    }

    public TableGroup(List<OrderTable> orderTables) {
        orderTables.forEach(
                orderTable -> orderTable.changeTableGroup(this));
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

    public void ungroup() {
        this.orderTables.forEach(orderTable -> orderTable.changeTableGroup(null));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TableGroup that = (TableGroup) o;
        return Objects.equals(id, that.id) && Objects.equals(createdDate, that.createdDate) && Objects.equals(orderTables, that.orderTables);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, createdDate, orderTables);
    }

    @Override
    public String toString() {
        return "TableGroup{" +
                "id=" + id +
                ", createdDate=" + createdDate +
                ", orderTables=" + orderTables +
                '}';
    }
}
