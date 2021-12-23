package kitchenpos.table.application;

import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import kitchenpos.common.exception.ErrorCode;
import kitchenpos.order.domain.OrderTableValidateEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.TableException;

@Component
public class TableValidateEventHandler {

	private final OrderTableRepository orderTableRepository;

	public TableValidateEventHandler(OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	@EventListener
	public void handle(OrderTableValidateEvent event) {
		OrderTable orderTable = orderTableRepository.findById(event.getOrderTableId())
			.orElseThrow(() -> {
				throw new TableException(ErrorCode.ORDER_TABLE_IS_NULL);
			});

		if (orderTable.isEmpty()) {
			throw new TableException(ErrorCode.ORDER_TABLE_IS_EMPTY);
		}
	}
}
