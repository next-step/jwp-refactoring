package kitchenpos.order.table.application;

import org.springframework.stereotype.Service;

import kitchenpos.order.table.client.TableClient;
import kitchenpos.order.table.client.dto.TableDto;

@Service
public class OrderTableService {

	private final TableClient tableClient;

	public OrderTableService(TableClient tableClient) {
		this.tableClient = tableClient;
	}

	public void changeEmpty(long orderTableId) {
		tableClient.changeEmpty(orderTableId);
	}

	public void ungroup(long orderTableId) {
		tableClient.ungroup(orderTableId);
	}

	public TableDto getTable(long orderTableId) {
		return tableClient.getTable(orderTableId);
	}
}
