package kitchenpos.order.application;


import static kitchenpos.menugroup.application.fixture.MenuGroupFixture.메뉴그룹_치킨류;
import static kitchenpos.menu.application.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.ordertable.application.fixture.OrderTableFixture.한명_주문테이블;
import static kitchenpos.product.application.fixture.ProductFixture.후리이드치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderValidator;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.product.domain.Product;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.order.dto.OrderStatusRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 관리 기능")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderValidator orderValidator;
    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("`주문`은 등록 할 수 있다.")
    void 주문_등록() {
        // given
        OrderRequest 요청_주문 = 요청_주문();
        given(orderRepository.save(any())).willReturn(주문());

        // when
        OrderResponse 생성된_주문 = orderService.create(요청_주문);

        // then
        assertThat(생성된_주문).isNotNull();
    }

    @Test
    @DisplayName("`주문`목록을 조회 할 수 있다.")
    void 주문목록_조회() {
        // given
        given(orderRepository.findAll()).willReturn(Collections.singletonList(주문()));

        // when
        List<OrderResponse> 조회된_주문목록 = orderService.list();

        // then
        assertThat(조회된_주문목록).isNotNull();
    }

    @Test
    @DisplayName("`주문`의 `주문 상태`를 변경 할 수 있다.")
    void 주문상태_변경() {
        // given
        Long 주문번호 = 1L;
        OrderStatus 변경할_상태 = OrderStatus.COMPLETION;
        OrderStatusRequest 상태변경_파라미터 = new OrderStatusRequest(변경할_상태);
        given(orderRepository.findById(주문번호)).willReturn(Optional.of(주문()));

        // when
        OrderResponse 상태변경된_주문 = orderService.changeOrderStatus(주문번호, 상태변경_파라미터);

        // then
        assertThat(상태변경된_주문.getOrderStatus()).isEqualTo(변경할_상태.name());
    }

    private OrderRequest 요청_주문() {
        List<OrderLineItemRequest> orderLineItemRequests = Collections.singletonList(
            new OrderLineItemRequest(null, 1L));
        return new OrderRequest(1L, orderLineItemRequests);
    }

    private Order 주문() {
        Menu 메뉴 = 메뉴();
        OrderTable 주문테이블 = 한명_주문테이블();
        OrderLineItemRequest 주문항목 = new OrderLineItemRequest(메뉴.getId(), 1);

        return Order.of(1L, Collections.singletonList(OrderLineItem.of(1L, 1L)));
    }

    private Menu 메뉴() {
        Product 치킨 = 후리이드치킨();
        MenuProduct 메뉴_치킨 = 메뉴상품(치킨);
        MenuGroup 메뉴_그룹 = 메뉴그룹_치킨류();
        return Menu.of("메뉴이름", 14000, 메뉴_그룹.getId(), Collections.singletonList(메뉴_치킨));
    }
}
