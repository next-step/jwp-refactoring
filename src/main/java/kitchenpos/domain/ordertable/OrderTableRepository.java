package kitchenpos.domain.ordertable;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderTableRepository extends JpaRepository<OrderTable, Long> {
    List<OrderTable> findAllByIdIn(List<Long> orderTableIds);
    int countAllByIdIn(List<Long> id);
    int countAllByIdInAndEmptyIs(List<Long> id, boolean empty);
}
