package kitchenpos.infrastructure.jpa.adapter;


import kitchenpos.domain.OrderTable;
import kitchenpos.port.OrderTablePort;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderTableJpaAdapter implements OrderTablePort {
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
        return null;
    }

    @Override
    public List<OrderTable> findAllByTableGroupId(Long tableGroupId) {
        return null;
    }
}
