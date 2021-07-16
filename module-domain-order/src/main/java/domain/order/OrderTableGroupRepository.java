package domain.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderTableGroupRepository extends JpaRepository<OrderTableGroup, Long> {
}
