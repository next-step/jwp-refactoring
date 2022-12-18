package kitchenpos.dao;

import kitchenpos.table.domain.OrderTable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OrderTableDaoImpl implements OrderTableDao{

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
