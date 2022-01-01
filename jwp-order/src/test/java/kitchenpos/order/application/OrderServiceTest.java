package kitchenpos.order.application;

import kitchenpos.order.fixture.TestMenuFactory;
import kitchenpos.order.fixture.TestOrderFactory;
import kitchenpos.menu.domain.Menu;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.dto.OrderResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {
    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderValidator orderValidator;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문을 등록한다.")
    @Test
    void saveOrder() {
        final Order 주문 = TestOrderFactory.주문_생성_Cooking_단계(1L);

        given(orderRepository.save(any())).willReturn(주문);

        final OrderResponse actual = orderService.create(TestOrderFactory.주문_생성_요청(1L));

        TestOrderFactory.주문_생성_확인됨(actual, 주문);
    }

    @DisplayName("등록된 주문을 조회한다.")
    @Test
    void findOrders() {
        final List<Order> 주문_목록 = TestOrderFactory.주문_목록_조회됨(1L , 10);

        given(orderRepository.findAll()).willReturn(주문_목록);

        final List<OrderResponse> actual = orderService.list();

        TestOrderFactory.주문_목록_확인됨(actual, 주문_목록);
    }

    @DisplayName("주문 상태를 완료로 변경한다.")
    @Test
    void changeOrderStatus() {
        final Menu 메뉴 = TestMenuFactory.메뉴_조회됨(1L, "메뉴", 10000, "메뉴그룹");
        final Order 주문 = TestOrderFactory.주문_meal_조회됨(10, 1L, 1L, 메뉴.getId(), 5);

        given(orderRepository.findById(anyLong())).willReturn(Optional.of(주문));

        final OrderResponse actual = orderService.changeOrderStatus(1L, TestOrderFactory.주문_완료_요청());

        TestOrderFactory.주문_완료_요청_확인됨(actual);
    }
}
