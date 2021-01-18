package kitchenpos.repository;

import java.util.List;
import java.util.Optional;
import kitchenpos.domain.OrderTable;
import org.springframework.data.repository.RepositoryDefinition;

@RepositoryDefinition(idClass = Long.class, domainClass = OrderTable.class)
public interface OrderTableDao {
    OrderTable save(OrderTable entity);

    Optional<OrderTable> findById(Long id);

    List<OrderTable> findAll();

    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroupId(Long tableGroupId);
}
