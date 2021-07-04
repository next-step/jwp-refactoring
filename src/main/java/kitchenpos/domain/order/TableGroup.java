package kitchenpos.domain.order;

import org.springframework.util.CollectionUtils;

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

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    private LocalDateTime createdDate;

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(null, orderTables, LocalDateTime.now());
    }
    // for jpa
    public TableGroup() {
    }

    private TableGroup(Long id, List<OrderTable> orderTables, LocalDateTime createdDate) {
        this.id = id;
        setOrderTables(orderTables);
        this.createdDate = createdDate;
    }

    private void setOrderTables(List<OrderTable> orderTables) {

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new IllegalArgumentException("should have over 2 orderTables");
        }

        this.orderTables = orderTables;
    }

    public Long getId() {
        return id;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public List<OrderTable> getOrderTables() {
        return orderTables;
    }

    public void changeOrderTables(List<OrderTable> orderTables) {
        this.orderTables = orderTables;
        orderTables.forEach(orderTable ->  {

            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new IllegalArgumentException("should have not empty savedOrderTable");
            }

            orderTable.changeTableGroup(this);
            orderTable.changeNonEmptyTable();
        });
    }
}
