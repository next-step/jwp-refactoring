package kitchenpos.ordertable.application;

import kitchenpos.common.exception.CannotFindException;
import kitchenpos.menu.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.ordertable.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static kitchenpos.common.exception.Message.*;
import static kitchenpos.menu.MenuTestFixture.맥모닝콤보;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.ordertable.OrderTableTestFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private MenuRepository menuRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderLineItemRepository orderLineItemRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderService orderService;

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void 주문_등록() {
        //Given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(비어있지_않은_테이블));
        when(menuRepository.findById(any())).thenReturn(Optional.of(맥모닝콤보));
        when(orderRepository.save(any())).thenReturn(첫번째_주문);

        //When
        orderService.create(첫번째_주문_요청);

        //Then
        verify(orderRepository, times(1)).save(any());
        verify(orderLineItemRepository, times(1)).saveAll(any());
    }

    @DisplayName("등록되지 않은 주문 테이블로 주문 생성시, 예외 발생한다")
    @Test
    void 등록되지_않은_주문_테이블로_주문_생성시_예외발생() {
        //When + Then
        Throwable 주문테이블_없음_예외 = catchThrowable(() -> orderService.create(첫번째_주문_요청));
        assertThat(주문테이블_없음_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_ORDER_SHOULD_HAVE_REGISTERED_TABLE.showText());
    }

    @DisplayName("등록되지 않은 메뉴로 주문 생성시, 예외 발생한다")
    @Test
    void 등록되지_않은_메뉴로_주문_생성시_예외발생() {
        //Given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(비어있지_않은_테이블));

        //When + Then
        Throwable 메뉴_없음_예외 = catchThrowable(() -> orderService.create(첫번째_주문_요청));
        assertThat(메뉴_없음_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_ORDER_SHOULD_HAVE_REGISTERED_MENU.showText());
    }

    @DisplayName("주문 항목이 1개 미만인 주문 생성시, 예외 발생한다")
    @Test
    void 주문_항목이_1개_미만_주문_생성시_예외발생() {
        //Given
        OrderRequest 주문_요청 = new OrderRequest(비어있지_않은_테이블.getId(), Collections.EMPTY_LIST);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(비어있지_않은_테이블));

        //When + Then
        Throwable 메뉴_없음_예외 = catchThrowable(() -> orderService.create(주문_요청));
        assertThat(메뉴_없음_예외).isInstanceOf(IllegalArgumentException.class)
                .hasMessage(ERROR_ORDER_LINE_ITEMS_SHOULD_HAVE_AT_LEAST_ONE_ITEM.showText());
    }

    @DisplayName("주문 목록을 조회할 수 있다")
    @Test
    void 주문_목록_조회() {
        //Given
        when(orderRepository.findAll()).thenReturn(Arrays.asList(첫번째_주문));

        //When
        List<OrderResponse> 조회된_주문 = orderService.list();

        //Then
        verify(orderRepository, times(1)).findAll();
    }

    @DisplayName("주문 상태를 변경할 수 있다")
    @Test
    void 주문상태_변경() {
        //Given
        when(orderRepository.findById(첫번째_주문.getId())).thenReturn(Optional.of(첫번째_주문));

        //When
        OrderResponse 변경된_주문 = orderService.changeOrderStatus(첫번째_주문.getId(), new OrderStatusRequest(MEAL));

        //Then
        assertThat(변경된_주문.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @DisplayName("등록되지 않은 주문의 상태 변경시, 예외발생한다")
    @Test
    void 등록되지_않은_주문의_주문상태_변경() {
        //When + Then
        Throwable 주문없음_예외 = catchThrowable(() -> orderService.changeOrderStatus(첫번째_주문.getId(), new OrderStatusRequest(MEAL)));
        assertThat(주문없음_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_ORDER_NOT_FOUND.showText());
    }
}