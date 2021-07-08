package kitchenpos.domain.order;

import kitchenpos.exception.InvalidOrderTableException;
import org.springframework.util.CollectionUtils;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
public class TableGroup implements Serializable {

    private static final long serialVersionUID = 1L;

    public static final TableGroup EMPTY = new TableGroup();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "tableGroup", cascade = CascadeType.ALL)
    private List<OrderTable> orderTables = new ArrayList<>();

    private LocalDateTime createdDate;

    // for jpa
    public TableGroup() {
    }

    private TableGroup(Long id, List<OrderTable> orderTables, LocalDateTime createdDate) {
        this.id = id;

        if (CollectionUtils.isEmpty(orderTables) || orderTables.size() < 2) {
            throw new InvalidOrderTableException("should have over 2 orderTables");
        }
        orderTables.forEach(orderTable -> orderTable.changeTableGroup(this));
        this.orderTables = orderTables;
        this.createdDate = createdDate;
    }

    private void initialSettingOrderTables(List<OrderTable> orderTables) {
        orderTables.forEach(orderTable -> {

            if (!orderTable.isEmpty() || Objects.nonNull(orderTable.getTableGroup())) {
                throw new InvalidOrderTableException("should have not empty savedOrderTable");
            }

            orderTable.changeTableGroup(this);
            orderTable.changeNonEmptyTable();
        });
    }

    public static TableGroup of(List<OrderTable> orderTables) {
        return new TableGroup(null, orderTables, LocalDateTime.now());
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
        initialSettingOrderTables(orderTables);
        this.orderTables = orderTables;
    }
}
