package kitchenpos.order.application;

import kitchenpos.exception.CannotFindException;
import kitchenpos.menu.domain.*;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.domain.OrderTableRepository;
import kitchenpos.product.domain.Product;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.common.Message.*;
import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
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
    OrderTableRepository orderTableRepository;

    @InjectMocks
    OrderService orderService;

    private final MenuGroup 첫번째_메뉴그룹 = new MenuGroup("메뉴그룹");
    private final Product 첫번째_상품 = new Product("첫번째 상품", Price.valueOf(13000));
    private final MenuProduct 첫번째_메뉴상품 = new MenuProduct(첫번째_상품, Quantity.of(1L));
    private final Menu 첫번째_메뉴 = new Menu("첫번째 메뉴", Price.valueOf(13000), 첫번째_메뉴그룹.getId(), Arrays.asList(첫번째_메뉴상품));

    private final OrderTable 첫번째_테이블 = new OrderTable(3, false);
    private final OrderLineItem 주문_항목_첫번째 = new OrderLineItem(첫번째_메뉴, Quantity.of(3L));

    private Order 첫번째_주문 = new Order(첫번째_테이블.getId(), COOKING, new OrderLineItems(Arrays.asList(주문_항목_첫번째)));

    @DisplayName("주문을 등록할 수 있다")
    @Test
    void 주문_등록() {
        //Given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(첫번째_테이블));
        when(menuRepository.findById(any())).thenReturn(Optional.of(첫번째_메뉴));
        when(orderRepository.save(any())).thenReturn(첫번째_주문);

        //When
        orderService.create(OrderRequest.of(첫번째_주문));

        //Then
        verify(orderRepository, times(1)).save(any());
    }

    @DisplayName("등록되지 않은 주문 테이블로 주문 생성시, 예외 발생한다")
    @Test
    void 등록되지_않은_주문_테이블로_주문_생성시_예외발생() {
        //When + Then
        Throwable 주문테이블_없음_예외 = catchThrowable(() -> orderService.create(OrderRequest.of(첫번째_주문)));
        assertThat(주문테이블_없음_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_ORDER_SHOULD_HAVE_REGISTERED_TABLE.showText());
    }

    @DisplayName("등록되지 않은 메뉴로 주문 생성시, 예외 발생한다")
    @Test
    void 등록되지_않은_메뉴로_주문_생성시_예외발생() {
        //Given
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(첫번째_테이블));

        //When + Then
        Throwable 메뉴_없음_예외 = catchThrowable(() -> orderService.create(OrderRequest.of(첫번째_주문)));
        assertThat(메뉴_없음_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_ORDER_SHOULD_HAVE_REGISTERED_MENU.showText());
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
        OrderResponse 변경된_주문 = orderService.changeOrderStatus(첫번째_주문.getId(), OrderStatusRequest.of(MEAL));

        //Then
        assertThat(변경된_주문.getOrderStatus()).isEqualTo(MEAL.name());
    }

    @DisplayName("등록되지 않은 주문의 상태 변경시, 예외발생한다")
    @Test
    void 등록되지_않은_주문의_주문상태_변경() {
        //When + Then
        Throwable 주문없음_예외 = catchThrowable(() -> orderService.changeOrderStatus(첫번째_주문.getId(), OrderStatusRequest.of(MEAL)));
        assertThat(주문없음_예외).isInstanceOf(CannotFindException.class)
                .hasMessage(ERROR_ORDER_NOT_FOUND.showText());
    }
}