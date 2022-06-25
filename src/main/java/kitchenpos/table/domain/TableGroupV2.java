package kitchenpos.table.domain;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupResponse;
import org.springframework.data.annotation.CreatedDate;

@Entity
@Table(name = "table_group")
public class TableGroupV2 {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    @CreatedDate
    private LocalDateTime createdDate;

    @OneToMany(mappedBy = "tableGroup", fetch = FetchType.LAZY, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderTableV2> orderTables = new ArrayList<>();

    protected TableGroupV2() {
    }

    public TableGroupV2(Long id, LocalDateTime createdDate) {
        this.id = id;
        this.createdDate = createdDate;
    }

    public TableGroupV2(Long id, LocalDateTime createdDate, List<OrderTableV2> orderTables) {
        this.id = id;
        this.createdDate = createdDate;
        this.orderTables = orderTables;
    }

    public TableGroupV2(LocalDateTime createdDate) {
        this(null, createdDate, null);
    }

    public void addOrderTable(OrderTableV2 orderTable) {
        if (this.orderTables == null) {
            this.orderTables = new ArrayList<>();
        }
        this.orderTables.add(orderTable);
    }

    public Long getId() {
        return id;
    }

    public TableGroupResponse toTableGroupResponse() {
        final List<OrderTableResponse> orderTableResponses = this.orderTables.stream()
                .map(OrderTableV2::toOrderTableResponse)
                .collect(Collectors.toList());
        return new TableGroupResponse(this.id, this.createdDate, orderTableResponses);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        TableGroupV2 that = (TableGroupV2) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
