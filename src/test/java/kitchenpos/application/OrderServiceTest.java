package kitchenpos.application;

import static kitchenpos.utils.generator.OrderFixtureGenerator.주문_생성;
import static kitchenpos.utils.generator.OrderFixtureGenerator.주문_생성_요청_객체;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있는_주문_테이블_생성;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.application.order.OrderService;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItemRepository;
import kitchenpos.domain.order.OrderRepository;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.ChangeOrderStatusRequest;
import kitchenpos.dto.order.CreateOrderRequest;
import kitchenpos.dto.order.CreateOrderTableItemRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.utils.generator.ScenarioTestFixtureGenerator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:Order")
class OrderServiceTest extends ScenarioTestFixtureGenerator {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderLineItemRepository orderLineItemRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderTable 비어있지_않은_주문_테이블_생성;
    private Order order;
    private CreateOrderRequest 커플_냉삼_메뉴_주문;

    @BeforeEach
    public void setUp() {
        super.setUp();
        비어있지_않은_주문_테이블_생성 = 비어있지_않은_주문_테이블_생성();
        order = 주문_생성(비어있지_않은_주문_테이블_생성, 커플_냉삼_메뉴);
        커플_냉삼_메뉴_주문 = 주문_생성_요청_객체(비어있지_않은_주문_테이블_생성, 커플_냉삼_메뉴);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        // Given
        given(menuRepository.findById(any())).willReturn(Optional.of(커플_냉삼_메뉴));
        given(orderTableRepository.findById(any())).willReturn(Optional.of(비어있지_않은_주문_테이블_생성));
        given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        orderService.create(커플_냉삼_메뉴_주문);

        // Then
        verify(menuRepository).findById(any());
        verify(orderTableRepository).findById(any());
        verify(orderRepository).save(any(Order.class));
    }

    @Test
    @DisplayName("주문 항목이 없는 주문을 생성하는 경우 예외 발생 검증")
    public void throwException_WhenOrderLineItemIsEmpty() {
        // When & Then
        assertThatExceptionOfType(NullPointerException.class)
            .isThrownBy(() -> orderService.create(new CreateOrderRequest()));
    }

    @Test
    @DisplayName("존재하지 않는 메뉴를 주문한 경우 예외 발생 검증")
    public void throwException_WhenOrderMenuCountIsOverThanPersistMenusCount() {
        CreateOrderTableItemRequest 존재하지_않는_메뉴의_주문_항목 = new CreateOrderTableItemRequest(1L, 1L);
        CreateOrderRequest 존재하지_않는_메뉴의_주문_항목이_포함된_주문_생성_요청 = new CreateOrderRequest(1L,
            Collections.singletonList(존재하지_않는_메뉴의_주문_항목));

        given(menuRepository.findById(anyLong())).willThrow(IllegalArgumentException.class);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(존재하지_않는_메뉴의_주문_항목이_포함된_주문_생성_요청));

        verify(menuRepository).findById(anyLong());
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않는 주문을 생성하는 경우 예외 발생 검증")
    public void throwException_WhenOrderTableIsNotExist() {
        // Given
        given(menuRepository.findById(any())).willReturn(Optional.of(커플_냉삼_메뉴));
        given(orderTableRepository.findById(any())).willThrow(IllegalArgumentException.class);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(커플_냉삼_메뉴_주문));

        verify(menuRepository).findById(any());
        verify(orderTableRepository).findById(any());
    }

    @Test
    @DisplayName("주문에 포함된 주문테이블이 비어있는 경우(주문을 요청한 테이블이 `isEmpty() = true`인 경우) 예외가 발생 검증")
    public void throwException_When() {
        given(menuRepository.findById(any())).willReturn(Optional.of(커플_냉삼_메뉴));
        given(orderTableRepository.findById(any())).willReturn(Optional.of(비어있는_주문_테이블_생성()));

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.create(커플_냉삼_메뉴_주문));

        verify(menuRepository).findById(any());
        verify(orderTableRepository).findById(any());
    }

    @Test
    @DisplayName("주문 목록을 조회한다.")
    public void getAllOrders() {
        // Given
        given(orderRepository.findAll()).willReturn(Collections.singletonList(order));

        // When
        orderService.list();

        // Then
        verify(orderRepository).findAll();
    }

    @Test
    @DisplayName("주문 상태를 변경한다.")
    public void changeOrderStatus() {
        // Given
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        ChangeOrderStatusRequest updateOrderRequest = new ChangeOrderStatusRequest(OrderStatus.MEAL);

        // When
        OrderResponse actualOrder = orderService.changeOrderStatus(order.getId(), updateOrderRequest);

        // Then
        verify(orderRepository).findById(any());
        assertThat(actualOrder.getOrderStatus()).isEqualTo(updateOrderRequest.getOrderStatus());
    }

    @Test
    @DisplayName("존재하지 않는 주문의 주문 상태를 수정하는 경우 예외 발생 검증")
    public void throwException_WhenOrderIsNotExist() {
        // Given
        given(orderRepository.findById(any())).willThrow(IllegalArgumentException.class);
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(OrderStatus.MEAL);

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.changeOrderStatus(any(), changeOrderStatusRequest));

        verify(orderRepository).findById(any());
    }

    @Test
    @DisplayName("주문 상태 값이 없는 주문의 주문 상태를 수정하는 경우 예외 발생 검증")
    public void throwException_WhenOrderStatusIsNull() {
        // Given
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest();
        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.changeOrderStatus(order.getId(), changeOrderStatusRequest));

        verify(orderRepository).findById(any());
    }

    @Test
    @DisplayName("완료 상태인 주문의 주문 상태를 수정하는 경우 예외 발생 검증")
    public void throwException_WhenOrderStatusIsCompletion() {
        // Given
        order.changeOrderStatus(OrderStatus.COMPLETION);
        ChangeOrderStatusRequest changeOrderStatusRequest = new ChangeOrderStatusRequest(OrderStatus.MEAL);

        given(orderRepository.findById(any())).willReturn(Optional.of(order));

        // When & Then
        assertThatExceptionOfType(IllegalArgumentException.class)
            .isThrownBy(() -> orderService.changeOrderStatus(any(), changeOrderStatusRequest));

        verify(orderRepository).findById(any());
    }
}
