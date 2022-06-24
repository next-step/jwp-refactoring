package kitchenpos.application;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderLineItemDao;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.orderLineItem.OrderLineItem;
import kitchenpos.domain.orderTable.OrderTable;
import kitchenpos.domain.orderTable.OrderTableRepository;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 관련 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderDao orderDao;

    @Mock
    OrderLineItemDao orderLineItemDao;

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderService orderService;

    @DisplayName("주문을 생성할 수 있다")
    @Test
    void create() {
        // given
        OrderTable orderTable = 주문_테이블_데이터_생성(1L, null, 2, false);
        List<OrderLineItem> orderLineItems = 주문_항목_목록_데이터_생성();
        OrderRequest request = 주문_요청_데이터_생성(1L, OrderStatus.COOKING.name(), orderLineItems);
        Order 예상값 = 주문_데이터_생성(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), orderLineItems);
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));
        given(orderDao.save(any(Order.class))).willReturn(예상값);

        // when
        OrderResponse 주문_생성_결과 = 주문_생성(request);

        // then
        주문_데이터_비교(주문_생성_결과, OrderResponse.of(예상값));
    }

    @DisplayName("주문을 생성할 수 있다 - 주문 항목 1개 이상 있어야 한다")
    @Test
    void create_exception1() {
        // given
        List<OrderLineItem> orderLineItems = new ArrayList<>();
        OrderRequest request = 주문_요청_데이터_생성(1L, OrderStatus.COOKING.name(), orderLineItems);

        // when && then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성할 수 있다 - 유효한 메뉴이어야 한다")
    @Test
    void create_exception2() {
        // given
        List<OrderLineItem> orderLineItems = 주문_항목_목록_데이터_생성();
        OrderRequest request = 주문_요청_데이터_생성(1L, OrderStatus.COOKING.name(), orderLineItems);
        given(menuRepository.countByIdIn(anyList())).willReturn(1L);

        // when && then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성할 수 있다 - 주문 테이블이 빈 테이블이 아니어야 한다")
    @Test
    void create_exception3() {
        // given
        OrderTable orderTable = 주문_테이블_데이터_생성(1L, null, 2, true);
        OrderRequest request = 주문_요청_데이터_생성(1L, OrderStatus.COOKING.name(), 주문_항목_목록_데이터_생성());
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(orderTable));

        // when && then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문을 생성할 수 있다 - 중복된 메뉴가 있어서는 안된다")
    @Test
    void create_exception4() {
        // given
        List<OrderLineItem> orderLineItems = new ArrayList<>(주문_항목_목록_데이터_생성());
        orderLineItems.add(주문_항목_데이터_생성(3L, 1L, 2L, 3));
        OrderRequest request = 주문_요청_데이터_생성(1L, OrderStatus.COOKING.name(), orderLineItems);
        given(menuRepository.countByIdIn(anyList())).willReturn(2L);

        // when && then
        assertThatThrownBy(() -> orderService.create(request))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 목록을 조회할 수 있다")
    @Test
    void list() {
        // given
        List<Order> 예상값 = Arrays.asList(
                주문_데이터_생성(1L, 1L, OrderStatus.COOKING.name(), LocalDateTime.now(), 주문_항목_목록_데이터_생성()),
                주문_데이터_생성(2L, 2L, OrderStatus.COOKING.name(), LocalDateTime.now(), 주문_항목_목록_데이터_생성())
        );
        given(orderDao.findAll()).willReturn(예상값);

        // when
        List<OrderResponse> 주문_목록_조회_결과 = 주문_목록_조회();

        // then
        assertAll(
                () -> 주문_데이터_비교(주문_목록_조회_결과.get(0), OrderResponse.of(예상값.get(0))),
                () -> 주문_데이터_비교(주문_목록_조회_결과.get(1), OrderResponse.of(예상값.get(1)))
        );
    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void changeOrderStatus() {
        // given
        Order 변경전_주문 = 주문_데이터_생성(1L, 1L, OrderStatus.MEAL.name(), LocalDateTime.now(), 주문_항목_목록_데이터_생성());
        OrderRequest 변경후_주문 = 주문_요청_데이터_생성(1L, OrderStatus.MEAL.name(), 주문_항목_목록_데이터_생성());
        given(orderDao.findById(1L)).willReturn(Optional.of(변경전_주문));

        // when
        OrderResponse 주문_상태_변경_결과 = 주문_상태_변경(1L, 변경후_주문);

        // then
        assertThat(주문_상태_변경_결과.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태를 변경할 수 있다 - 주문 상태가 '계산 완료' 상태가 아니어야 한다")
    @Test
    void changeOrderStatus_exception1() {
        // given
        Order 변경전_주문 = 주문_데이터_생성(1L, 1L, OrderStatus.COMPLETION.name(), LocalDateTime.now(), 주문_항목_목록_데이터_생성());
        OrderRequest 변경후_주문 = 주문_요청_데이터_생성(1L, OrderStatus.MEAL.name(), 주문_항목_목록_데이터_생성());
        given(orderDao.findById(1L)).willReturn(Optional.of(변경전_주문));

        // when && then
        assertThatThrownBy(() -> 주문_상태_변경(1L, 변경후_주문)).isInstanceOf(IllegalArgumentException.class);
    }

    private OrderLineItem 주문_항목_데이터_생성(Long seq, Long orderId, Long menuId, long quantity) {
        return new OrderLineItem(seq, orderId, menuId, quantity);
    }

    private OrderRequest 주문_요청_데이터_생성(Long orderTableId, String orderStatus, List<OrderLineItem> orderLineItems) {
        return new OrderRequest(orderTableId, orderStatus, orderLineItems);
    }

    private Order 주문_데이터_생성(Long id, Long orderTableId, String orderStatus, LocalDateTime orderedTime, List<OrderLineItem> orderLineItems) {
        return new Order(id, orderTableId, orderStatus, orderedTime, orderLineItems);
    }

    private List<OrderLineItem> 주문_항목_목록_데이터_생성() {
        OrderLineItem orderLineItemId1 = 주문_항목_데이터_생성(1L, 1L, 1L, 3);
        OrderLineItem orderLineItemId2 = 주문_항목_데이터_생성(2L, 1L, 2L, 3);
        return Arrays.asList(orderLineItemId1, orderLineItemId2);
    }

    private OrderResponse 주문_생성(OrderRequest orderRequest) {
        return orderService.create(orderRequest);
    }

    private List<OrderResponse> 주문_목록_조회() {
        return orderService.list();
    }

    private OrderResponse 주문_상태_변경(Long orderId, OrderRequest orderRequest) {
        return orderService.changeOrderStatus(orderId, orderRequest);
    }

    private void 주문_데이터_비교(OrderResponse 주문_생성_결과, OrderResponse 예상값) {
        assertAll(
                () -> assertThat(주문_생성_결과.getId()).isEqualTo(예상값.getId()),
                () -> assertThat(주문_생성_결과.getOrderTableId()).isEqualTo(예상값.getOrderTableId()),
                () -> assertThat(주문_생성_결과.getOrderStatus()).isEqualTo(예상값.getOrderStatus()),
                () -> assertThat(주문_생성_결과.getOrderLineItems()).isEqualTo(예상값.getOrderLineItems())
        );
    }
}
