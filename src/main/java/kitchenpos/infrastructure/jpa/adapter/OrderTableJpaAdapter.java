package kitchenpos.infrastructure.jpa.adapter;


import kitchenpos.domain.OrderTable;
import kitchenpos.infrastructure.jpa.repository.OrderTableJpaRepository;
import kitchenpos.port.OrderTablePort;
import org.springframework.stereotype.Repository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional
public class OrderTableJpaAdapter implements OrderTablePort {

    private final OrderTableJpaRepository orderTableJpaRepository;

    public OrderTableJpaAdapter(OrderTableJpaRepository orderTableJpaRepository) {
        this.orderTableJpaRepository = orderTableJpaRepository;
    }

    @Override
    public OrderTable save(OrderTable entity) {
        return orderTableJpaRepository.save(entity);
    }

    @Override
    @Transactional(readOnly = true)
    public OrderTable findById(Long id) {
        return orderTableJpaRepository.findById(id)
                .orElseThrow(IllegalArgumentException::new);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderTable> findAll() {
        return orderTableJpaRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderTable> findAllByIdIn(List<Long> ids) {
        return orderTableJpaRepository.findAllByIdIn(ids);
    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return orderTableJpaRepository.findAllByTableGroupId(tableGroupId);
    }
}
