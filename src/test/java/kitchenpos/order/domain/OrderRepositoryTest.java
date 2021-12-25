package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

@DisplayName("주문 JPA 테스트")
@DataJpaTest
@Sql(scripts = "classpath:scripts/data.sql")
public class OrderRepositoryTest {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private MenuRepository menuRepository;

    @Test
    @DisplayName("주문 저장 시 주문 항목도 저장")
    void save() {
        Menu menu = menuRepository.findById(1L).get();
        OrderLineItem orderLineItem = OrderLineItem.of(OrderMenu.of(menu.getId(), menu.getName(), menu.getPrice()), 2L);

        Order persist = orderRepository.save(Order.of(1L, null, Arrays.asList(orderLineItem)));

        //query 확인
        orderRepository.flush();

        assertAll(
            () -> assertTrue(persist.isOnGoing(), "상태 체크"),
            () -> assertNotNull(persist.getOrderedTime(), "주문 시간 체크"),
            () -> assertThat(persist.getOrderLineItems())
                .extracting(OrderLineItem::getQuantity).containsExactly(2L)
        );
    }

}
