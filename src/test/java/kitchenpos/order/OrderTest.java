package kitchenpos.order;

import kitchenpos.dao.OrderDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class OrderTest {

    @Autowired
    private OrderDao orderDao;

    @Test
    @DisplayName("주문을 등록합니다.")
    void save() {
        // when
        Order persistOrder = 주문_등록_요청(OrderStatus.COOKING, 1L);

        // then
        assertThat(persistOrder.getId()).isNotNull();
        assertThat(persistOrder.getOrderStatus()).isEqualTo(OrderStatus.COOKING.name());
    }

    private Order 주문_등록_요청(OrderStatus orderStatus, Long orderTableId) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTableId);

        return this.orderDao.save(order);
    }


    @Test
    @DisplayName("특정 주문을 조회합니다.")
    void findById() {
        // given
        Order persistOrder = 주문_등록_요청(OrderStatus.COOKING, 1L);

        // when
        Order foundOrder = this.orderDao.findById(persistOrder.getId()).get();

        // then
        assertThat(foundOrder.getId()).isEqualTo(persistOrder.getId());
        assertThat(foundOrder.getOrderedTime()).isEqualTo(persistOrder.getOrderedTime());
    }


    @Test
    @DisplayName("전체 주문을 조회합니다.")
    void findAll() {
        // given
        주문_등록_요청(OrderStatus.COOKING, 1L);
        주문_등록_요청(OrderStatus.COOKING, 2L);
        주문_등록_요청(OrderStatus.COOKING, 3L);

        // when
        List<Order> foundOrders = this.orderDao.findAll();

        // then
        assertThat(foundOrders).hasSize(3);
    }

    @Test
    @DisplayName("테이블ID와 주문상태로 주문이 있는지 확인합니다.")
    void existsByOrderTableIdAndOrderStatusIn() {
        // given
        Long orderTableId = 1L;
        주문_등록_요청(OrderStatus.COOKING, orderTableId);
        주문_등록_요청(OrderStatus.MEAL, orderTableId);

        // when
        boolean existsByOrderTableIdAndOrderStatusIn
                = this.orderDao.existsByOrderTableIdAndOrderStatusIn(orderTableId
                        , Arrays.asList(new String[]{OrderStatus.COOKING.name(), OrderStatus.MEAL.name()}));

        // then
        assertTrue(existsByOrderTableIdAndOrderStatusIn);
    }


    @Test
    @DisplayName("여러개의 테이블ID와 주문상태로 주문이 있는지 확인합니다.")
    void existsByOrderTableIdInAndOrderStatusIn() {
        // given
        Long orderTableId1 = 1L;
        Long orderTableId2 = 2L;
        주문_등록_요청(OrderStatus.COOKING, orderTableId1);
        주문_등록_요청(OrderStatus.MEAL, orderTableId2);

        // when
        boolean existsByOrderTableIdAndOrderStatusIn
                = this.orderDao.existsByOrderTableIdInAndOrderStatusIn(
                        Arrays.asList(new Long[]{orderTableId1, orderTableId2})
                        , Arrays.asList(new String[]{OrderStatus.COOKING.name(), OrderStatus.MEAL.name()}));

        // then
        assertTrue(existsByOrderTableIdAndOrderStatusIn);
    }
}
