package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderLineItem;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.TableServiceTest.주문_테이블_데이터_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 관련 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    MenuDao menuDao;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableDao orderTableDao;

    @InjectMocks
    OrderService orderService;

    @DisplayName("주문 생성")
    @Test
    void create() {
        // given
        OrderTable orderTable = 주문_테이블_데이터_생성(1L, null, 2, false);
        List<OrderLineItem> orderLineItems = 주문_항목_목록_데이터_생성();
        Order request = 주문_데이터_생성(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        Order 예상값 = 주문_데이터_생성(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderDao.save(request)).willReturn(예상값);

        // when
        Order 주문_생성_결과 = 주문_생성(request);

        // then
        주문_데이터_비교(주문_생성_결과, 예상값);
    }

    @DisplayName("주문 생성 - 주문 항목 없을 경우")
    @Test
    void create_exception1() {
        // given
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        Order request = 주문_데이터_생성(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);

        // when && then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 - 유효한 메뉴 id가 아닐 경우")
    @Test
    void create_exception2() {
        // given
        List<OrderLineItem> orderLineItems = 주문_항목_목록_데이터_생성();
        Order request = 주문_데이터_생성(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        given(menuDao.countByIdIn(anyList())).willReturn(1L);

        // when && then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 생성 - 주문 테이블이 빈 테이블일 경우")
    @Test
    void create_exception3() {
        // given
        OrderTable orderTable = 주문_테이블_데이터_생성(1L, null, 2, true);
        Order request = 주문_데이터_생성(null, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), 주문_항목_목록_데이터_생성());
        given(menuDao.countByIdIn(anyList())).willReturn(2L);
        given(orderTableDao.findById(1L)).willReturn(Optional.of(orderTable));

        // when && then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록 조회")
    @Test
    void list() {
        // given
        List<Order> 예상값 = Arrays.asList(
                주문_데이터_생성(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), 주문_항목_목록_데이터_생성()),
                주문_데이터_생성(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now(), 주문_항목_목록_데이터_생성())
        );
        given(orderDao.findAll()).willReturn(예상값);

        // when
        List<Order> 주문_목록_조회_결과 = 주문_목록_조회();

        // then
        assertAll(
                () -> 주문_데이터_비교(주문_목록_조회_결과.get(0), 예상값.get(0)),
                () -> 주문_데이터_비교(주문_목록_조회_결과.get(1), 예상값.get(1))
        );
    }

    @DisplayName("주문 상태 변경")
    @Test
    void changeOrderStatus() {
        // given
        Order 변경전_주문 = 주문_데이터_생성(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), 주문_항목_목록_데이터_생성());
        Order 변경후_주문 = 주문_데이터_생성(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), 주문_항목_목록_데이터_생성());
        given(orderDao.findById(1L)).willReturn(Optional.of(변경전_주문));

        // when
        Order 주문_상태_변경_결과 = 주문_상태_변경(1L, 변경후_주문);

        // then
        assertThat(주문_상태_변경_결과.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태 변경 - 변경전 주문 상태가 '계산 완료' 상태인 경우")
    @Test
    void changeOrderStatus_exception1() {
        // given
        Order 변경전_주문 = 주문_데이터_생성(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), 주문_항목_목록_데이터_생성());
        Order 변경후_주문 = 주문_데이터_생성(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), 주문_항목_목록_데이터_생성());
        given(orderDao.findById(1L)).willReturn(Optional.of(변경전_주문));

        // when && then
        assertThatThrownBy(() -> 주문_상태_변경(1L, 변경후_주문)).isInstanceOf(IllegalArgumentException.class);
    }

    private OrderLineItem 주문_항목_데이터_생성(Long seq, Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }

    private Order 주문_데이터_생성(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    private List<OrderLineItem> 주문_항목_목록_데이터_생성() {
        OrderLineItem orderLineItemId1 = 주문_항목_데이터_생성(1L, 1L, 1L, 3);
        OrderLineItem orderLineItemId2 = 주문_항목_데이터_생성(2L, 1L, 2L, 3);
        return Arrays.asList(orderLineItemId1, orderLineItemId2);
    }

    private Order 주문_생성(Order order) {
        return orderService.create(order);
    }

    private List<Order> 주문_목록_조회() {
        return orderService.list();
    }

    private Order 주문_상태_변경(Long orderId, Order order) {
        return orderService.changeOrderStatus(orderId, order);
    }

    private void 주문_데이터_비교(Order 주문_생성_결과, Order 예상값) {
        assertAll(
                () -> assertThat(주문_생성_결과.getId()).isEqualTo(예상값.getId()),
                () -> assertThat(주문_생성_결과.getOrderTableId()).isEqualTo(예상값.getOrderTableId()),
                () -> assertThat(주문_생성_결과.getOrderStatus()).isEqualTo(예상값.getOrderStatus()),
                () -> assertThat(주문_생성_결과.getOrderLineItems()).isEqualTo(예상값.getOrderLineItems())
        );
    }
}
