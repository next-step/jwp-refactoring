package kitchenpos.order.domain;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderMenuRepository extends JpaRepository<OrderMenu, Long> {
    List<OrderMenu> findAllByOrder(Order order);

    List<OrderMenu> findAllByOrderIn(List<Order> orders);
}
