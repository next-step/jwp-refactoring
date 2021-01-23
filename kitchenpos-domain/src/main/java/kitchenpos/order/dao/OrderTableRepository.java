package kitchenpos.order.dao;

import kitchenpos.order.domain.TableGroup;
import kitchenpos.order.domain.OrderTable;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(List<Long> ids);

    List<OrderTable> findAllByTableGroup(TableGroup tableGroup);
}
