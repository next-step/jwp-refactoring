package kitchenpos.application;

import static kitchenpos.utils.generator.OrderFixtureGenerator.주문_생성;
import static kitchenpos.utils.generator.OrderFixtureGenerator.주문_생성_요청_객체;
import static kitchenpos.utils.generator.OrderTableFixtureGenerator.비어있지_않은_주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.Collections;
import java.util.Optional;
import kitchenpos.order.application.OrderCreationValidator;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.CreateOrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.repository.OrderRepository;
import kitchenpos.table.domain.OrderTable;
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
    private OrderCreationValidator orderCreationValidator;

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    private OrderTable 비어있지_않은_주문_테이블_생성;
    private Order order;
    private CreateOrderRequest 커플_냉삼_메뉴_주문_생성_요청;
    private OrderLineItem 커플_냉삼_메뉴_주문_항목;

    @BeforeEach
    public void setUp() {
        super.setUp();
        비어있지_않은_주문_테이블_생성 = 비어있지_않은_주문_테이블_생성();
        order = 주문_생성(비어있지_않은_주문_테이블_생성, 커플_냉삼_메뉴);
        커플_냉삼_메뉴_주문_생성_요청 = 주문_생성_요청_객체(비어있지_않은_주문_테이블_생성, 커플_냉삼_메뉴);
        커플_냉삼_메뉴_주문_항목 = new OrderLineItem(커플_냉삼_메뉴.getId(), 1);
    }

    @Test
    @DisplayName("주문을 생성한다.")
    void createOrder() {
        // Given
        given(orderRepository.save(any(Order.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        orderService.create(커플_냉삼_메뉴_주문_생성_요청);

        // Then
        verify(orderRepository).save(any(Order.class));
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
