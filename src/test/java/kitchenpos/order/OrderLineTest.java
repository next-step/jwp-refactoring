package kitchenpos.order;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
public class OrderLineTest {

    @Autowired
    private OrderLineItemDao orderLineItemDao;

    @Autowired
    private OrderDao orderDao;


    @Test
    @DisplayName("주문항목을 등록합니다.")
    void save() {
        // given
        Order persistOrder = 주문_등록_요청(OrderStatus.COOKING, 1L);
        OrderLineItem persistOrderLineItem = 메뉴_등록_요청(persistOrder);

        // then
        assertThat(persistOrderLineItem.getSeq()).isNotNull();
        assertThat(persistOrderLineItem.getOrderId()).isEqualTo(persistOrder.getId());
    }

    private OrderLineItem 메뉴_등록_요청(Order persistOrder) {
        OrderLineItem orderLineItem = new OrderLineItem();
        orderLineItem.setOrderId(persistOrder.getId());
        orderLineItem.setMenuId(1L);
        orderLineItem.setQuantity(4);

        // when
        return this.orderLineItemDao.save(orderLineItem);
    }

    public Order 주문_등록_요청(OrderStatus orderStatus, Long orderTableId) {
        Order order = new Order();
        order.setOrderStatus(orderStatus.name());
        order.setOrderedTime(LocalDateTime.now());
        order.setOrderTableId(orderTableId);

        return this.orderDao.save(order);
    }

    @Test
    @DisplayName("특정 주문항목을 조회합니다.")
    void findById() {
        // given
        Order persistOrder = 주문_등록_요청(OrderStatus.COOKING, 1L);
        OrderLineItem persistOrderLineItem = 메뉴_등록_요청(persistOrder);

        // when
        OrderLineItem foundOrderLineItem = this.orderLineItemDao.findById(persistOrderLineItem.getSeq()).get();

        // then
        assertThat(foundOrderLineItem.getSeq()).isEqualTo(persistOrderLineItem.getSeq());
        assertThat(foundOrderLineItem.getOrderId()).isEqualTo(persistOrderLineItem.getOrderId());
    }


    @Test
    @DisplayName("전체 주문항목을 조회합니다.")
    void findAll() {
        // given
        Order persistOrder = 주문_등록_요청(OrderStatus.COOKING, 1L);
        메뉴_등록_요청(persistOrder);
        메뉴_등록_요청(persistOrder);

        // when
        List<OrderLineItem> foundOrderLineItems = this.orderLineItemDao.findAll();

        // then
        assertThat(foundOrderLineItems).hasSize(2);
    }


    @Test
    @DisplayName("주문ID로 특정 주문항목을 조회합니다.")
    void findAllByOrderId() {
        // given
        Order persistOrder = 주문_등록_요청(OrderStatus.COOKING, 1L);
        메뉴_등록_요청(persistOrder);
        메뉴_등록_요청(persistOrder);

        // when
        List<OrderLineItem> foundOrderLineItems = this.orderLineItemDao.findAllByOrderId(persistOrder.getId());

        // then
        assertThat(foundOrderLineItems).hasSize(2);
    }

}
