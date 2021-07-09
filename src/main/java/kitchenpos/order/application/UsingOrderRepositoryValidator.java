package kitchenpos.order.application;

import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.TableValidator;
import org.springframework.stereotype.Component;

@Component
public class UsingOrderRepositoryValidator implements TableValidator {

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
}
