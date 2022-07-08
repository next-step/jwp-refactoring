package kitchenpos.order.validator;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.menugroup.domain.MenuGroup;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.NoSuchElementException;

import static kitchenpos.common.Messages.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderValidatorTest {

    @InjectMocks
    private OrderValidator orderValidator;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Test
    @DisplayName("주문 생성시 정상적으로 유효성 검사에 성공한다.")
    void validateCreateOrder() {
        MenuProduct 피자_2판 = MenuProduct.of(1L, Quantity.of(2));
        MenuProducts 피자_구성품 = MenuProducts.of(Arrays.asList(피자_2판));
        MenuGroup 피자_2판_메뉴_그룹 = MenuGroup.of(Name.of("피자_2판_메뉴_그룹"));

        Menu 기본_메뉴 = Menu.of(
                Name.of("피자_두판_세트_메뉴"),
                Price.of(BigDecimal.valueOf(30_000)),
                피자_2판_메뉴_그룹.getId(),
                피자_구성품
        );

        // given
        OrderRequest 주문_생성_요청 = OrderRequest.of(
                1L,
                Arrays.asList(OrderLineItemRequest.of(1L, 1L))
        );

        // when
        when(orderTableRepository.existsById(any())).thenReturn(Boolean.TRUE);
        when(menuRepository.findByIdIn(any())).thenReturn(Arrays.asList(기본_메뉴));

        // then
        assertDoesNotThrow(() -> orderValidator.validateCreateOrder(주문_생성_요청));
    }

    @Test
    @DisplayName("주문 생성시 주문 테이블 정보가 없는 경우 유효성 검사에 실패 된다.")
    void orderTableNotExists() {
        // given
        OrderRequest 주문_생성_요청 = OrderRequest.of(
                1L,
                Arrays.asList(OrderLineItemRequest.of(1L, 1L))
        );

        when(orderTableRepository.existsById(any())).thenReturn(false);

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> orderValidator.validateCreateOrder(주문_생성_요청))
                .withMessage(ORDER_TABLE_NOT_EXISTS);
    }

    @Test
    @DisplayName("주문 생성시 주문 항목 정보가 없는 경우 유효성 검사에 실패 된다.")
    void orderLineItemRequired() {
        // given
        OrderRequest 주문_생성_요청 = OrderRequest.of(
                1L,
                null
        );

        when(orderTableRepository.existsById(any())).thenReturn(true);

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderValidator.validateCreateOrder(주문_생성_요청))
                .withMessage(ORDER_LINE_ITEM_REQUIRED);
    }

    @Test
    @DisplayName("주문 생성시 메뉴 정보가 없는 경우 유효성 검사에 실패 된다.")
    void menuFindInNoSuch() {
        // given
        OrderRequest 주문_생성_요청 = OrderRequest.of(
                1L,
                Arrays.asList(OrderLineItemRequest.of(1L, 1L))
        );

        when(orderTableRepository.existsById(any())).thenReturn(true);
        when(menuRepository.findByIdIn(any())).thenReturn(Collections.emptyList());

        assertThatExceptionOfType(NoSuchElementException.class)
                .isThrownBy(() -> orderValidator.validateCreateOrder(주문_생성_요청))
                .withMessage(MENU_FIND_IN_NO_SUCH);
    }

    @Test
    @DisplayName("주문 상태 변경시 주문 상태가 유효성 검사에 성공한다.")
    void validateChangeOrderStatus() {
        MenuProduct 메뉴_치킨 = MenuProduct.of(1L, Quantity.of(1L));
        MenuGroup 추천_메뉴 = MenuGroup.of(1L, Name.of("추천 메뉴"));
        Menu 치킨_두마리 = Menu.of(
                1L,
                Name.of("치킨"),
                Price.of(BigDecimal.valueOf(17_000)),
                추천_메뉴.getId(),
                MenuProducts.of(Arrays.asList(메뉴_치킨))
        );

        OrderLineItem 주문_항목 = OrderLineItem.of(치킨_두마리.getId(), Quantity.of(2));

        Order 신규_주문 = Order.of(1L, 1L, OrderStatus.COOKING, OrderLineItems.of(Arrays.asList(주문_항목)));

        assertDoesNotThrow(() -> orderValidator.validateChangeOrderStatus(신규_주문));
    }

    @Test
    @DisplayName("주문 상태 변경시 주문 상태가 완성 상태인 경우 유효성 검사에 실패 된다.")
    void orderStatusChangeCannotCompletion() {
        MenuProduct 메뉴_치킨 = MenuProduct.of(1L, Quantity.of(1L));
        MenuGroup 추천_메뉴 = MenuGroup.of(1L, Name.of("추천 메뉴"));
        Menu 치킨_두마리 = Menu.of(
                1L,
                Name.of("치킨"),
                Price.of(BigDecimal.valueOf(17_000)),
                추천_메뉴.getId(),
                MenuProducts.of(Arrays.asList(메뉴_치킨))
        );

        OrderLineItem 주문_항목 = OrderLineItem.of(치킨_두마리.getId(), Quantity.of(2));

        Order 신규_주문 = Order.of(1L, 1L, OrderStatus.COMPLETION, OrderLineItems.of(Arrays.asList(주문_항목)));

        assertThatExceptionOfType(IllegalArgumentException.class)
                .isThrownBy(() -> orderValidator.validateChangeOrderStatus(신규_주문))
                .withMessage(ORDER_STATUS_CHANGE_CANNOT_COMPLETION);
    }
}
