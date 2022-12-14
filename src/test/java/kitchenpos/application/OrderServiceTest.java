package kitchenpos.application;

import static kitchenpos.domain.OrderLineItemTestFixture.*;
import static kitchenpos.domain.OrderTableTestFixture.orderTable;
import static kitchenpos.domain.OrderTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.Optional;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.menu.domain.MenuRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderLineItemDao orderLineItemDao;
    @Mock
    private OrderTableDao orderTableDao;
    @InjectMocks
    private OrderService orderService;

    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = orderTable(1L, null, 3, false);
    }

    @Test
    @DisplayName("주문 등록시 주문 항목은 필수이다.")
    void createOrderByOrderLineItemIsNull() {
        // given
        Order order = order(1L, 주문테이블.getId(), Collections.emptyList(), OrderStatus.COOKING.name());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(order));
    }

    @Test
    @DisplayName("주문 등록시 주문 항목은 모두 등록된 메뉴여야 한다.")
    void createOrderByCreatedMenu() {
        // given
        OrderLineItem 탕수육_1그릇 = orderLineItem(3L, null, 1);
        given(menuRepository.countByIdIn(
                Arrays.asList(짜장면_1그릇.getMenuId(), 짬뽕_2그릇.getMenuId(), 탕수육_1그릇.getMenuId())))
                .willReturn(2L);
        Order order = order(1L, 주문테이블.getId(), Arrays.asList(짜장면_1그릇, 짬뽕_2그릇, 탕수육_1그릇), OrderStatus.COOKING.name());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(order));
    }

    @Test
    @DisplayName("주문 등록시 주문 테이블은 등록된 테이블이어야 한다.")
    void createOrderByCreatedOrderTable() {
        // given
        given(menuRepository.countByIdIn(Arrays.asList(짜장면_1그릇.getMenuId(), 짬뽕_2그릇.getMenuId())))
                .willReturn(2L);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.empty());
        Order order = order(1L, 주문테이블.getId(), Arrays.asList(짜장면_1그릇, 짬뽕_2그릇), OrderStatus.COOKING.name());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(order));
    }

    @Test
    @DisplayName("주문 등록시 주문 테이블은 비어있는 테이블일 수 없다.")
    void createOrderByEmptyOrderTable() {
        // given
        OrderTable emptyTable = orderTable(2L, null, 0, true);
        given(menuRepository.countByIdIn(Arrays.asList(짜장면_1그릇.getMenuId(), 짬뽕_2그릇.getMenuId())))
                .willReturn(2L);
        given(orderTableDao.findById(emptyTable.getId())).willReturn(Optional.of(emptyTable));
        Order order = order(1L, emptyTable.getId(), Arrays.asList(짜장면_1그릇, 짬뽕_2그릇), OrderStatus.COOKING.name());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(order));
    }

    @Test
    @DisplayName("주문을 등록한다.")
    void createOrder() {
        // given
        given(menuRepository.countByIdIn(Arrays.asList(짜장면_1그릇.getMenuId(), 짬뽕_2그릇.getMenuId())))
                .willReturn(2L);
        given(orderTableDao.findById(주문테이블.getId())).willReturn(Optional.of(주문테이블));
        Order order = order(1L, 주문테이블.getId(), Arrays.asList(짜장면_1그릇, 짬뽕_2그릇), OrderStatus.COOKING.name());
        given(orderDao.save(order)).willReturn(order);

        // when
        Order actual = orderService.create(order);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(Order.class)
        );
    }
    
    @Test
    @DisplayName("주문 상태를 변경하려면 주문이 등록되어야 한다.")
    void updateOrderStatusByNoneOrdered() {
        // given
        Order changeOrderStatusRequest = changeOrderStatusRequest(OrderStatus.MEAL.name());
        given(orderDao.findById(주문테이블.getId())).willReturn(Optional.empty());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(주문테이블.getId(), changeOrderStatusRequest));
    }

    @Test
    @DisplayName("주문 상태를 변경하려면 주문상태가 완료가 아니어야 한다.")
    void updateOrderStatusByOrderStatusIsNotEqualToCompleted() {
        // given
        Order changeOrderStatusRequest = changeOrderStatusRequest(OrderStatus.MEAL.name());
        Order order = order(2L, 주문테이블.getId(), Arrays.asList(짜장면_1그릇, 짬뽕_2그릇), OrderStatus.COMPLETION.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrderStatusRequest));
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    void updateOrderStatus() {
        // given
        Order changeOrderStatusRequest = changeOrderStatusRequest(OrderStatus.MEAL.name());
        Order order = order(1L, 주문테이블.getId(), Arrays.asList(짜장면_1그릇, 짬뽕_2그릇), OrderStatus.COOKING.name());
        given(orderDao.findById(order.getId())).willReturn(Optional.of(order));

        // when
        Order actual = orderService.changeOrderStatus(order.getId(), changeOrderStatusRequest);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
    
    @Test
    @DisplayName("주문 목록을 조회하면 주문 목록이 반환된다.")
    void test() {
        
    }
}
