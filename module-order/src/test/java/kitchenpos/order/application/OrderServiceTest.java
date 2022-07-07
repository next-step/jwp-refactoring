package kitchenpos.order.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.*;
import kitchenpos.order.dto.*;
import kitchenpos.order.validator.OrderValidator;
import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.NumberOfGuests;
import kitchenpos.table.domain.OrderTable;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

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
    private OrderValidator orderValidator;

    @Test
    @DisplayName("주문 테이블에 기본메뉴를 주문한다면 정상적으로 테이블 등록이 된다")
    void create() {
        // given
        OrderTable 신규_주문_테이블 = OrderTable.of(1L, null, NumberOfGuests.of(4), Empty.of(false));
        MenuGroup 추천_메뉴_그룹 = MenuGroup.of(1L, Name.of("추천 메뉴"));
        MenuProduct 메뉴_상품_치킨 = MenuProduct.of(1L, null, 1L, Quantity.of(1L));
        Menu 치킨_두마리 = Menu.of(
                1L,
                Name.of("치킨"),
                Price.of(BigDecimal.valueOf(17_000)),
                추천_메뉴_그룹.getId(),
                MenuProducts.of(Arrays.asList(메뉴_상품_치킨))
        );
        OrderLineItem 주문_항목 = OrderLineItem.of(치킨_두마리.getId(), Quantity.of(2));
        Order 신규_주문 = Order.of(1L, 신규_주문_테이블.getId(), OrderStatus.COOKING, OrderLineItems.of(Arrays.asList(주문_항목)));

        OrderRequest 주문_생성_요청 = OrderRequest.of(
                신규_주문_테이블.getId(),
                Arrays.asList(OrderLineItemRequest.of(치킨_두마리.getId(), 1L))
        );

        // when
        when(orderRepository.save(any())).thenReturn(신규_주문);

        OrderResponse 주문_생성_결과 = orderService.create(주문_생성_요청);

        Assertions.assertThat(주문_생성_결과).isEqualTo(OrderResponse.of(신규_주문));
    }

    @Test
    @DisplayName("주문건 리스트를 조회 한다면 정상적으로 조회 된다")
    void list() {
        // given
        MenuProduct 메뉴_상품_치킨 = MenuProduct.of(1L, null, 1L, Quantity.of(1L));
        MenuGroup 추천_메뉴_그룹 = MenuGroup.of(1L, Name.of("추천 메뉴"));
        Menu 치킨_두마리 = Menu.of(
                1L,
                Name.of("치킨"),
                Price.of(BigDecimal.valueOf(17_000)),
                추천_메뉴_그룹.getId(),
                MenuProducts.of(Arrays.asList(메뉴_상품_치킨))
        );
        OrderLineItem 주문_항목 = OrderLineItem.of(치킨_두마리.getId(), Quantity.of(2));
        OrderTable 신규_주문_테이블 = OrderTable.of(1L, null, NumberOfGuests.of(4), Empty.of(false));
        Order 신규_주문 = Order.of(1L, 신규_주문_테이블.getId(), OrderStatus.COOKING, OrderLineItems.of(Arrays.asList(주문_항목)));

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
        MenuProduct 메뉴_상품_치킨 = MenuProduct.of(1L, null, 1L, Quantity.of(1L));
        MenuGroup 추천_메뉴_그룹 = MenuGroup.of(1L, Name.of("추천 메뉴"));
        Menu 치킨_두마리 = Menu.of(
                1L,
                Name.of("치킨"),
                Price.of(BigDecimal.valueOf(17_000)),
                추천_메뉴_그룹.getId(),
                MenuProducts.of(Arrays.asList(메뉴_상품_치킨))
        );
        OrderLineItem 주문_항목 = OrderLineItem.of(치킨_두마리.getId(), Quantity.of(2));
        OrderTable 변경_예정_주문_테이블 = OrderTable.of(1L, null, NumberOfGuests.of(4), Empty.of(false));
        Order 변경_예정_주문 = Order.of(1L, 변경_예정_주문_테이블.getId(), OrderStatus.COOKING, OrderLineItems.of(Arrays.asList(주문_항목)));
        ChangeOrderStatusRequest 상태_변경_요청 = ChangeOrderStatusRequest.of(OrderStatus.MEAL.name());

        // when
        when(orderRepository.findById(any())).thenReturn(Optional.of(변경_예정_주문));
        when(orderRepository.save(any())).thenReturn(변경_예정_주문);

        ChangeOrderStatusResponse 주문_상태_변경_결과 = orderService.changeOrderStatus(변경_예정_주문.getId(), 상태_변경_요청);

        // then
        Assertions.assertThat(주문_상태_변경_결과.getOrderStatus()).isEqualTo(OrderStatus.MEAL.name());
    }
}
