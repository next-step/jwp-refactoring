package kitchenpos.order.application;

import kitchenpos.fixture.*;
import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    private OrderTable 주문_테이블;
    private Order 주문;
    private OrderLineItem 주문_메뉴;

    private Product 상품;
    private MenuProduct 메뉴상품;
    private MenuGroup 메뉴그룹;
    private Menu 메뉴;

    private OrderRequest 주문_요청;

    @BeforeEach
    void set_up() {
        주문_테이블 = OrderTableFixture.create(1, false);
        ReflectionTestUtils.setField(주문_테이블, "id", 1L);
        주문 = OrderFixture.create(주문_테이블);
        ReflectionTestUtils.setField(주문, "id", 1L);

        메뉴그룹 = MenuGroupFixture.create("메뉴그룹");
        ReflectionTestUtils.setField(메뉴그룹, "id", 1L);
        메뉴 = MenuFixture.create("메뉴", BigDecimal.valueOf(1000), 메뉴그룹);
        ReflectionTestUtils.setField(메뉴, "id", 1L);
        상품 = ProductFixture.create("상품", BigDecimal.valueOf(1000L));
        ReflectionTestUtils.setField(상품, "id", 1L);
        메뉴상품 = MenuProductFixture.create(메뉴, 상품, 2L);


        주문_메뉴 = new OrderLineItem(주문, 메뉴.getId(), 1);
        OrderLineItemRequest 주문_항목_요청 = new OrderLineItemRequest(1L, 1L);
        주문_요청 = new OrderRequest(1L, Arrays.asList(주문_항목_요청));
    }

    @DisplayName("주문을 등록할 수 있다.")
    @Test
    void create() {
        // given
        OrderRequest request = new OrderRequest(주문_테이블.getId(), OrderLineItemRequest.list(Arrays.asList(주문_메뉴)));

        when(orderTableRepository.findById(주문_테이블.getId())).thenReturn(Optional.of(주문_테이블));
        when(menuRepository.findAllById(any())).thenReturn(Arrays.asList(메뉴));
        when(orderRepository.save(any(Order.class))).thenReturn(주문);

        // when
        OrderResponse 주문_등록 = orderService.create(request);

        // then
        assertThat(주문_등록.getOrderLineItems()).hasSize(주문.getOrderLineItems().size());
    }

    @DisplayName("주문 항목이 없으면 주문할 수 없다.")
    @Test
    void create_error_without_order_item() {
        // given && when && then
        assertThatThrownBy(() -> orderService.create(주문_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목의 메뉴의 개수가 일치하지 않으면 주문 할 수 없다.")
    @Test
    void create_error_duplicated_order_item() {
        // given
        when(orderTableRepository.findById(주문_테이블.getId())).thenReturn(Optional.of(주문_테이블));
        when(menuRepository.findAllById(any())).thenReturn(Arrays.asList(메뉴, 메뉴));

        // when && then
        assertThatThrownBy(() -> orderService.create(주문_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 값이 저장되어 있지 않으면 주문 할 수 없다.")
    @Test
    void create_error_order_table_empty() {
        // given
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        // when && then
        assertThatThrownBy(() -> orderService.create(주문_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 테이블의 값이 비어있다면 주문 할 수 없다.")
    @Test
    void create_error_order_table_value_empty() {
        // given
        Menu 메뉴 = MenuFixture.create("name", BigDecimal.valueOf(1000), 메뉴그룹);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(OrderTableFixture.create(1, false)));
        when(menuRepository.findAllById(any())).thenReturn(Arrays.asList(메뉴));
        주문_테이블.changeEmptyStatus(true);

        // when && then
        assertThatThrownBy(() -> orderService.create(주문_요청))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 목록을 조회할 수 있다.")
    @Test
    void list() {
        when(orderRepository.findAll()).thenReturn(Arrays.asList(주문));

        List<OrderResponse> 주문_목록_조회_결과 = orderService.list();

        assertAll(
                () -> assertThat(주문_목록_조회_결과).hasSize(1),
                () -> assertThat(주문_목록_조회_결과.get(0).getOrderStatus()).isEqualTo(주문.getOrderStatus().name())
        );
    }

    @DisplayName("주문의 주문 상태가 조리 또는 식사중 일 경우 상태를 변경할 수 있다.")
    @ParameterizedTest(name = "#{index} - 주문 상태를 {2}에서 {3}로 변경할 수 있다.")
    @MethodSource("order_status_info")
    void update_order_status(OrderStatus 기존주문상태, OrderStatus 변경주문상태, String s, String s2) {
        // given
        OrderStatusRequest 주문상태_식사중_변경 = new OrderStatusRequest(변경주문상태);

        when(orderRepository.findById(any())).thenReturn(Optional.of(주문));
        when(orderRepository.save(any())).thenReturn(주문);

        // when
        OrderResponse 주문_상태_변경_결과 = orderService.changeOrderStatus(주문.getId(), 주문상태_식사중_변경);

        // then
        assertThat(주문_상태_변경_결과.getOrderStatus()).isEqualTo(변경주문상태.name());
    }

    private static Stream<Arguments> order_status_info() {
        return Stream.of(
                Arguments.of(OrderStatus.MEAL, OrderStatus.COOKING, "조리", "식사"),
                Arguments.of(OrderStatus.COOKING, OrderStatus.MEAL, "식사", "조리"),
                Arguments.of(OrderStatus.MEAL, OrderStatus.COMPLETION, "조리", "계산 완료"),
                Arguments.of(OrderStatus.COOKING, OrderStatus.COMPLETION, "식사", "계산 완료")
        );
    }

    @DisplayName("주문이 저장되어 있지 않으면 주문의 상태를 변경할 수 없다.")
    @Test
    void update_fail_order_status_not_save_order() {
        // given
        OrderStatusRequest 주문상태_식사중_변경 = new OrderStatusRequest(OrderStatus.MEAL);

        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문상태_식사중_변경))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문의 상태가 완료인 경우 주문의 상태를 변경할 수 없다.")
    @Test
    void update_fail_not_change_order_complete() {
        // given
        OrderStatusRequest 주문상태_식사중_변경 = new OrderStatusRequest(OrderStatus.MEAL);
        주문.changeOrderStatus(OrderStatus.COMPLETION);

        when(orderRepository.findById(any())).thenReturn(Optional.of(주문));

        // when && then
        assertThatThrownBy(() -> orderService.changeOrderStatus(주문.getId(), 주문상태_식사중_변경))
                .isInstanceOf(IllegalArgumentException.class);
    }

}
