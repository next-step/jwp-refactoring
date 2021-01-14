package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.dto.OrderDto;
import kitchenpos.dto.OrderStatus;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(idClass = Long.class, domainClass = Order.class)
public interface OrderDao {
    Order save(Order entity);

    Optional<Order> findById(Long id);

    List<Order> findAll();

    boolean existsByOrderTableIdAndOrderStatusIn(Long orderTableId, List<OrderStatus> orderStatuses);

    boolean existsByOrderTableIdInAndOrderStatusIn(List<Long> orderTableIds, List<OrderStatus> orderStatuses);
}
