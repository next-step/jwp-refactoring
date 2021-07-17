package kitchenpos.tableGroup.dto;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import kitchenpos.domain.OrderTableRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tableGroup.domain.TableGroup;

public class TableGroupRequest {
    private Long id;
    private LocalDateTime createdDate;
    private List<OrderTableRequest> orderTables;

    public TableGroupRequest() {
	}

	public TableGroupRequest(LocalDateTime createdDate, List<OrderTableRequest> orderTables) {
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

    public List<OrderTableRequest> getOrderTables() {
        return orderTables;
    }

    public void setOrderTables(final List<OrderTableRequest> orderTables) {
        this.orderTables = orderTables;
    }

    public TableGroup toTableGroup() {
    	List<OrderTable> orderTables = this.orderTables.stream()
			.map(orderTableRequest -> orderTableRequest.toOrderTable())
			.collect(Collectors.toList());

    	return new TableGroup(createdDate, orderTables);
	}
}
