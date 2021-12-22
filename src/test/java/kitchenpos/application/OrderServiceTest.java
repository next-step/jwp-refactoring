package kitchenpos.application;


import static kitchenpos.application.fixture.MenuGroupFixture.메뉴그룹_치킨류;
import static kitchenpos.application.fixture.MenuProductFixture.메뉴상품;
import static kitchenpos.application.fixture.OrderTableFixture.빈_테이블;
import static kitchenpos.application.fixture.ProductFixture.후리이드치킨;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.dao.MenuDao;
import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.domain.menu.Menu;
import kitchenpos.domain.menu.MenuGroup;
import kitchenpos.domain.menu.MenuProduct;
import kitchenpos.domain.order.Order;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.OrderStatus;
import kitchenpos.domain.order.OrderTable;
import kitchenpos.domain.product.Product;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import kitchenpos.dto.order.OrderResponse;
import kitchenpos.dto.order.OrderStatusRequest;
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
    private MenuDao menuDao;
    @Mock
    private OrderDao orderDao;
    @Mock
    private OrderTableDao orderTableDao;

    private final long 주문테이블번호 = 1L;
    private final long 주문수량 = 1L;

    @InjectMocks
    private OrderService orderService;

    @Test
    @DisplayName("`주문`은 등록 할 수 있다.")
    void 주문_등록() {
        // given
        OrderRequest 요청_주문 = 요청_주문();
        given(orderTableDao.findById(any())).willReturn(Optional.of(빈_테이블()));
        given(menuDao.findAllById(any())).willReturn(Collections.singletonList(메뉴()));
        given(orderDao.save(any())).willReturn(주문());

        // when
        OrderResponse 생성된_주문 = orderService.create(요청_주문);

        // then
        assertThat(생성된_주문).isNotNull();
    }

    @Test
    @DisplayName("`주문`목록을 조회 할 수 있다.")
    void 주문목록_조회() {
        // given
        given(orderDao.findAll()).willReturn(Collections.singletonList(주문()));

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
        given(orderDao.findById(주문번호)).willReturn(Optional.of(주문()));

        // when
        OrderResponse 상태변경된_주문 = orderService.changeOrderStatus(주문번호, 상태변경_파라미터);

        // then
        assertThat(상태변경된_주문.getOrderStatus()).isEqualTo(변경할_상태.name());
    }

    private OrderRequest 요청_주문() {
        List<OrderLineItemRequest> orderLineItemRequests = Collections.singletonList(
            new OrderLineItemRequest(null, 주문수량));
        OrderRequest 요청_주문 = new OrderRequest(주문테이블번호, orderLineItemRequests);
        return 요청_주문;
    }

    private Order 주문() {
        Menu 메뉴 = 메뉴();
        OrderTable 주문테이블 = 빈_테이블();
        OrderLineItemRequest 주문항목 = new OrderLineItemRequest(메뉴.getId(), 1);

        return Order.of(주문테이블, Collections.singletonList(OrderLineItem.of(메뉴, 1L)));
    }

    private Menu 메뉴() {
        Product 치킨 = 후리이드치킨(1L);
        MenuProduct 메뉴_치킨 = 메뉴상품(치킨);
        MenuGroup 메뉴_그룹 = 메뉴그룹_치킨류();
        return Menu.of("메뉴이름", 14000, 메뉴_그룹, Collections.singletonList(메뉴_치킨));
    }
}
