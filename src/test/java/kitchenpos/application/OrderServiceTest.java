package kitchenpos.application;

import kitchenpos.fixture.MenuFixture;
import kitchenpos.fixture.MenuProductFixture;
import kitchenpos.fixture.OrderFixture;
import kitchenpos.fixture.OrderLineItemFixture;
import kitchenpos.fixture.TableFixture;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderDao;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.ChangeOrderStatusRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static kitchenpos.fixture.MenuGroupFixture.추천_메뉴_그룹;
import static kitchenpos.fixture.ProductFixture.강정치킨;
import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderDao orderDao;

    @Mock
    private MenuService menuService;

    @Mock
    private TableService tableService;

    private MenuProduct 강정치킨_두마리;
    private Menu 강정치킨_두마리_세트_메뉴;
    private OrderTable 일반_테이블;
    private OrderTable 비어있는_테이블;
    private OrderLineItem 주문_항목;
    private Order 신규_주문;
    private Order 완료_주문;

    @BeforeEach
    public void setUp() {
        강정치킨_두마리 = MenuProductFixture.create(1L, 강정치킨, 2);
        강정치킨_두마리_세트_메뉴 = MenuFixture.create(1L, "강정치킨_두마리_세트_메뉴", BigDecimal.valueOf(30_000), 추천_메뉴_그룹, Arrays.asList(강정치킨_두마리));
        일반_테이블 = TableFixture.create(1L, null, 4, false);
        비어있는_테이블 = TableFixture.create(1L, null, 4, true);
        주문_항목 = OrderLineItemFixture.create(null, null, 강정치킨_두마리_세트_메뉴, 1L);
        신규_주문 = OrderFixture.create(1L, 일반_테이블, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(주문_항목));
        완료_주문 = OrderFixture.create(1L, 일반_테이블, OrderStatus.COMPLETION, LocalDateTime.now(), Arrays.asList(주문_항목));
    }

    @DisplayName("주문 생성 성공 테스트")
    @Test
    void create_success() {
        // given
        OrderRequest orderRequest = OrderRequest.of(
                일반_테이블.getId(), Arrays.asList(OrderLineItemRequest.of(주문_항목.getMenu().getId(), 주문_항목.getQuantity().getQuantity())));

        given(tableService.findById(any(Long.class))).willReturn(일반_테이블);
        given(orderDao.save(any(Order.class))).willReturn(신규_주문);

        // when
        OrderResponse 생성된_주문 = orderService.create(orderRequest);

        // then
        assertThat(생성된_주문).isEqualTo(OrderResponse.of(신규_주문));
    }

    @DisplayName("주문 생성 실패 테스트 - 주문 항목 없음")
    @Test
    void create_failure_notExistOrderLineItems() {
        // given
        OrderRequest orderRequest = OrderRequest.of(일반_테이블.getId(), Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문 생성 실패 테스트 - 주문 테이블 존재하지 않음")
    @Test
    void create_failure_notFoundOrderTable() {
        // given
        OrderRequest orderRequest = OrderRequest.of(
                일반_테이블.getId(), Arrays.asList(OrderLineItemRequest.of(주문_항목.getMenu().getId(), 주문_항목.getQuantity().getQuantity())));

        given(tableService.findById(any(Long.class))).willThrow(new NoSuchElementException());

        // when & then
        assertThatThrownBy(() -> orderService.create(orderRequest))
                .isInstanceOf(NoSuchElementException.class);
    }

    @DisplayName("주문 생성 실패 테스트 - 주문 테이블 비어있음")
    @Test
    void create_failure_emptyOrderTable() {
        // given
        OrderRequest orderRequest = OrderRequest.of(
                비어있는_테이블.getId(), Arrays.asList(OrderLineItemRequest.of(주문_항목.getMenu().getId(), 주문_항목.getQuantity().getQuantity())));

        given(tableService.findById(any(Long.class))).willReturn(비어있는_테이블);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.create(orderRequest));
    }

    @DisplayName("주문 목록 조회 테스트")
    @Test
    void list() {
        // given
        given(orderDao.findAll()).willReturn(Arrays.asList(신규_주문));

        // when
        List<OrderResponse> 조회된_주문_목록 = orderService.list();

        // then
        assertThat(조회된_주문_목록).containsExactly(OrderResponse.of(신규_주문));
    }

    @DisplayName("주문 상태 수정 성공 테스트")
    @Test
    void changeOrderStatus_success() {
        // given
        ChangeOrderStatusRequest changeOrderStatusRequest = ChangeOrderStatusRequest.of(OrderStatus.MEAL.name());

        given(orderDao.findById(any(Long.class))).willReturn(Optional.of(신규_주문));

        // when
        OrderResponse 수정된_주문 = orderService.changeOrderStatus(신규_주문.getId(), changeOrderStatusRequest);

        // then
        assertThat(수정된_주문.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @DisplayName("주문 상태 수정 실패 테스트 - 수정 전 주문 상태가 COMPLETION")
    @Test
    void changeOrderStatus_failure_orderStatus() {
        // given
        ChangeOrderStatusRequest changeOrderStatusRequest = ChangeOrderStatusRequest.of(OrderStatus.MEAL.name());

        given(orderDao.findById(any(Long.class))).willReturn(Optional.of(완료_주문));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderService.changeOrderStatus(완료_주문.getId(), changeOrderStatusRequest));
    }
}
