package kitchenpos.ordertable.application;

import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.order.application.OrderQueryService;
import kitchenpos.order.domain.OrderList;
import kitchenpos.order.domain.Orders;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Service
public class OrderTableService {

	private final OrderQueryService orderQueryService;
	private final OrderTableQueryService orderTableQueryService;

	public OrderTableService(
		  OrderQueryService orderQueryService,
		  OrderTableQueryService orderTableQueryService) {
		this.orderQueryService = orderQueryService;
		this.orderTableQueryService = orderTableQueryService;
	}

	public OrderTableResponse create(final OrderTableRequest request) {
		OrderTable savedOrderTable = orderTableQueryService.save(request.newEntity());
		return OrderTableResponse.of(savedOrderTable);
	}

	public List<OrderTableResponse> list() {
		return orderTableQueryService.findAll().stream()
			  .map(OrderTableResponse::of)
			  .collect(Collectors.toList());
	}

	public OrderTableResponse changeEmpty(final Long orderTableId,
		  final OrderTableRequest request) {
		OrderTable savedOrderTable = orderTableQueryService.findById(orderTableId);
		List<Orders> savedOrders = orderQueryService.findAllByOrderTable(savedOrderTable);
		OrderList orderList = new OrderList(savedOrders);

		savedOrderTable.changeEmpty(request.isEmpty(), orderList.isCompleteAllOrders());
		return OrderTableResponse.of(savedOrderTable);
	}

	public OrderTableResponse changeNumberOfGuests(final Long orderTableId,
		  final OrderTableRequest request) {
		OrderTable orderTable = orderTableQueryService.findById(orderTableId);
		orderTable.changeNumberOfGuests(request.getNumberOfGuests());

		return OrderTableResponse.of(orderTable);
	}

	public void unGroup(TableGroup tableGroup) {
		List<OrderTable> savedOrderTables = orderTableQueryService.findAllByTableGroup(tableGroup);

		savedOrderTables.forEach(orderTable -> {
			List<Orders> orders = orderQueryService.findAllByOrderTable(orderTable);
			OrderList orderList = new OrderList(orders);
			orderTable.unGroup(orderList.isCompleteAllOrders());
		});
	}
}
