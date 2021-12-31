package kitchenpos.infra.ordertable;

import kitchenpos.core.ordertable.domain.OrderTable;
import kitchenpos.core.ordertable.domain.OrderTableRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderTableRepositoryImpl implements OrderTableRepository {
    private final JpaOrderTableRepository jpaOrderTableRepository;

    public OrderTableRepositoryImpl(JpaOrderTableRepository jpaOrderTableRepository) {
        this.jpaOrderTableRepository = jpaOrderTableRepository;
    }

    @Override
    public OrderTable save(OrderTable orderTable) {
        return jpaOrderTableRepository.save(orderTable);
    }

    @Override
    public List<OrderTable> findAll() {
        return jpaOrderTableRepository.findAll();
    }

    @Override
    public Optional<OrderTable> findById(Long id) {
        return jpaOrderTableRepository.findById(id);
    }

    @Override
    public List<OrderTable> findAllByIdIn(List<Long> orderTableIds) {
        return jpaOrderTableRepository.findAllByIdIn(orderTableIds);
    }

    @Override
    public List<OrderTable> findAllById(List<Long> orderTableIds) {
        return jpaOrderTableRepository.findAllById(orderTableIds);
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return jpaOrderTableRepository.findAllByTableGroupId(tableGroupId);
    }

    @Override
    public List<OrderTable> saveAll(List<OrderTable> orderTables) {
        return jpaOrderTableRepository.saveAll(orderTables);
    }
}
