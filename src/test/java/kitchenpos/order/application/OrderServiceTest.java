package kitchenpos.order.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.application.MenuService;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.fixture.MenuFixture;
import kitchenpos.menu.fixture.MenuProductFixture;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.menugroup.fixture.MenuGroupFixture;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.*;
import kitchenpos.product.fixture.ProductFixture;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static kitchenpos.common.Messages.ORDER_STATUS_CHANGE_CANNOT_COMPLETION;
import static kitchenpos.table.fixture.TableFixture.비어있는_주문_테이블_그룹_없음;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    private OrderService orderService;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private MenuService menuService;

    @Mock
    private TableService tableService;

    private ChangeOrderStatusRequest 상태_변경_요청;

    private OrderTable 신규_주문_테이블;
    private Order 신규_주문;
    private Order 완료_주문;
    private OrderLineItem 주문_항목;
    private MenuGroup 추천_메뉴_그룹;
    private MenuProduct 메뉴_상품_치킨;
    private Menu 치킨_두마리;

    @BeforeEach
    void setUp() {
        추천_메뉴_그룹 = MenuGroupFixture.create(1L, Name.of("추천 메뉴"));
        메뉴_상품_치킨 = MenuProductFixture.create(1L, ProductFixture.치킨, Quantity.of(1L));

        치킨_두마리 = MenuFixture.create(
                1L,
                Name.of("치킨"),
                Price.of(BigDecimal.valueOf(17_000)),
                추천_메뉴_그룹,
                MenuProducts.of(Arrays.asList(메뉴_상품_치킨))
        );
        주문_항목 = OrderLineItem.of(치킨_두마리, Quantity.of(2));
        신규_주문_테이블 = OrderTable.of(1L, null, NumberOfGuests.of(4), Empty.of(false));

        신규_주문 = Order.of(1L, 신규_주문_테이블, OrderStatus.COOKING, OrderLineItems.of(Arrays.asList(주문_항목)));
        완료_주문 = Order.of(2L, 신규_주문_테이블, OrderStatus.COMPLETION, OrderLineItems.of(Arrays.asList(주문_항목)));

        상태_변경_요청 = ChangeOrderStatusRequest.of(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문 테이블에 기본메뉴를 주문한다면 정상적으로 테이블 등록이 된다")
    void create() {
        // given
        OrderRequest 주문_생성_요청 = OrderRequest.of(
                신규_주문_테이블.getId(),
                Arrays.asList(OrderLineItemRequest.of(치킨_두마리.getId(), 1L))
        );

        // when
        when(orderRepository.save(any())).thenReturn(신규_주문);
        when(tableService.findById(any())).thenReturn(신규_주문_테이블);

        OrderResponse 주문_생성_결과 = orderService.create(주문_생성_요청);

        Assertions.assertThat(주문_생성_결과).isEqualTo(OrderResponse.of(신규_주문));
    }

    @Test
    @DisplayName("주문 테이블 등록시 등록되지 않은 테이블 정보를 입력한 경우 실패된다")
    void create2() {
        // given
        OrderRequest 주문_생성_요청 = OrderRequest.of(
                9999L,
                Arrays.asList(OrderLineItemRequest.of(치킨_두마리.getId(), 1L))
        );

        // when
        when(tableService.findById(any())).thenThrow(new NoSuchElementException());

        // then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> orderService.create(주문_생성_요청));
    }

    @Test
    @DisplayName("주문건 생성시 주문의 테이블이 비어있는 경우 주문건 생성이 실패된다")
    void create3() {
        // given
        OrderRequest 주문_생성_요청 = OrderRequest.of(
                비어있는_주문_테이블_그룹_없음.getId(),
                Arrays.asList(OrderLineItemRequest.of(치킨_두마리.getId(), 1L))
        );

        // when
        when(tableService.findById(any())).thenReturn(비어있는_주문_테이블_그룹_없음);

        // then
        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.create(주문_생성_요청));
    }

    @Test
    @DisplayName("주문건 리스트를 조회 한다면 정상적으로 조회 된다")
    void list() {
        // when
        when(orderRepository.findAll()).thenReturn(Arrays.asList(신규_주문));

        List<OrderResponse> 주문_목록_조회_결과 = orderService.list();

        // then
        assertAll(
                () -> Assertions.assertThat(주문_목록_조회_결과).hasSize(1),
                () -> Assertions.assertThat(주문_목록_조회_결과).containsExactly(OrderResponse.of(신규_주문))
        );
    }

    @Test
    @DisplayName("주문건 상태 변경시 정상적으로 조회 및 변경 가능한 상태라면 변경된다")
    void changeOrderStatus() {
        // given
        OrderTable 변경_예정_주문_테이블 = OrderTable.of(1L, null, NumberOfGuests.of(4), Empty.of(false));
        Order 변경_예정_주문 = Order.of(1L, 변경_예정_주문_테이블, OrderStatus.COOKING, OrderLineItems.of(Arrays.asList(주문_항목)));

        // when
        when(orderRepository.findById(any())).thenReturn(Optional.of(변경_예정_주문));
        when(orderRepository.save(any())).thenReturn(변경_예정_주문);

        ChangeOrderStatusResponse 주문_상태_변경_결과 = orderService.changeOrderStatus(변경_예정_주문.getId(), 상태_변경_요청);

        // then
        Assertions.assertThat(주문_상태_변경_결과.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }

    @Test
    @DisplayName("주문건 상태 변경을 요청시 정상적으로 주문건이 조회되지 않는다면 변경에 실패한다")
    void changeOrderStatus2() {
        // when
        when(orderRepository.findById(any())).thenReturn(Optional.empty());

        //then
        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(신규_주문.getId(), 상태_변경_요청));
    }

    @Test
    @DisplayName("주문건 상태 변경 요청시 주문건이 완료된 상태라면 주문건의 상태를 변경에 실패한다")
    void changeOrderStatus3() {
        // given
        OrderTable 변경_예정_주문_테이블 = OrderTable.of(1L, null, NumberOfGuests.of(4), Empty.of(false));
        Order 변경_예정_주문 = Order.of(1L, 변경_예정_주문_테이블, OrderStatus.COMPLETION, OrderLineItems.of(Arrays.asList(주문_항목)));

        when(orderRepository.findById(any())).thenReturn(Optional.ofNullable(변경_예정_주문));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderService.changeOrderStatus(변경_예정_주문.getId(), 상태_변경_요청))
                .withMessage(ORDER_STATUS_CHANGE_CANNOT_COMPLETION)
        ;
    }
}
