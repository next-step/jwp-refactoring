package kitchenpos.infrastructure.jpa.adapter;


import kitchenpos.domain.OrderTable;
import kitchenpos.infrastructure.jpa.repository.OrderTableJpaRepository;
import kitchenpos.port.OrderTablePort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderTableJpaAdapter implements OrderTablePort {

    private final OrderTableJpaRepository orderTableJpaRepository;

    public OrderTableJpaAdapter(OrderTableJpaRepository orderTableJpaRepository) {
        this.orderTableJpaRepository = orderTableJpaRepository;
    }

    @Override
    public OrderTable save(OrderTable entity) {
        return null;
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return Optional.empty();
    }

    @Override
    public List<OrderTable> findAll() {
        return null;
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return orderTableJpaRepository.findAllByIdIn(ids);
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return null;
    }
}
