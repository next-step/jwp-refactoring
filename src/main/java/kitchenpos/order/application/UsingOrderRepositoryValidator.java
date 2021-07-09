package kitchenpos.order.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.TableExternalValidator;
import kitchenpos.tablegroup.domain.TableGroupExternalValidator;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class UsingOrderRepositoryValidator implements TableExternalValidator, TableGroupExternalValidator {

  private final OrderRepository orderRepository;

  public UsingOrderRepositoryValidator(OrderRepository orderRepository) {
    this.orderRepository = orderRepository;
  }

  @Override
  public void validateTableInUse(Long orderTableId) {
    if (orderRepository.existsByOrderTableIdAndOrderStatusIn(
        orderTableId, OrderStatus.getBusyStatus())) {
      throw new IllegalArgumentException();
    }
  }

  @Override
  public void validateTablesInUse(List<Long> tableIds) {
    if (orderRepository.existsByOrderTableIdInAndOrderStatusIn(tableIds, OrderStatus.getBusyStatus())) {
      throw new IllegalArgumentException();
    }
  }
}
