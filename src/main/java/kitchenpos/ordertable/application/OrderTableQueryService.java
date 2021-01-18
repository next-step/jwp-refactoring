package kitchenpos.ordertable.application;

import java.util.List;
import java.util.Set;
import javax.persistence.EntityNotFoundException;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.tablegroup.domain.TableGroup;
import org.springframework.stereotype.Service;

@Service
public class OrderTableQueryService {
	private final OrderTableRepository orderTableRepository;

	public OrderTableQueryService(
		  OrderTableRepository orderTableRepository) {
		this.orderTableRepository = orderTableRepository;
	}

	public OrderTable findById(Long orderTableId) {
		return orderTableRepository.findById(orderTableId)
			  .orElseThrow(EntityNotFoundException::new);
	}

	public List<OrderTable> findAllByOrderTableIds(Set<Long> orderTableIds) {
		return orderTableRepository.findAllById(orderTableIds);
	}

	public List<OrderTable> findAllByTableGroup(TableGroup tableGroup) {
		return orderTableRepository.findAllByTableGroup(tableGroup);
	}
}
