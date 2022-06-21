package kitchenpos.domain;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrdersRepository extends JpaRepository<Orders, Long> {
    @EntityGraph(attributePaths = {"orderLineItems"})
    List<Orders> findAll();
}
