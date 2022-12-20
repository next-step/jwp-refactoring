package kitchenpos.infrastructure.jpa.adapter;

import kitchenpos.domain.Order;
import kitchenpos.infrastructure.jpa.repository.OrderJpaRepository;
import kitchenpos.port.OrderPort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static kitchenpos.constants.ErrorCodeType.ORDER_NOT_FOUND;

@Service
@Transactional
public class OrderJpaAdapter implements OrderPort {

    private final OrderJpaRepository orderJpaRepository;

    public OrderJpaAdapter(OrderJpaRepository orderJpaRepository) {
        this.orderJpaRepository = orderJpaRepository;
    }

    @Override
    public Order save(Order entity) {
        return orderJpaRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public Order findById(Long id) {
        return orderJpaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException(ORDER_NOT_FOUND.getMessage()));
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAll() {
        return orderJpaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Order> findAllByOrderTableIdIn(List<Long> orderTablesId) {
        return orderJpaRepository.findAllByOrderTableIdIn(orderTablesId);
    }

    @Override
    public List<Order> findByOrderTableId(Long orderTableID) {
        return orderJpaRepository.findByOrderTableId(orderTableID);
    }
}
