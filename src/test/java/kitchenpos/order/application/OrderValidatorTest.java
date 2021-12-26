package kitchenpos.order.application;

import kitchenpos.common.domain.Name;
import kitchenpos.common.domain.Price;
import kitchenpos.common.domain.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.MenuProducts;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.application.TableService;
import kitchenpos.table.domain.Empty;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
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

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("주문 유효성 검증 테스트")
class OrderValidatorTest {

    @InjectMocks
    private OrderValidator orderValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private TableService tableService;

    private Long orderTableId;
    private Long productId;
    private Long menuGroupId;
    private Long menuId;

    @BeforeEach
    public void setUp() {
        // given
        orderTableId = 1L;
        productId = 1L;
        menuGroupId = 1L;
        menuId = 1L;
    }

    @DisplayName("주문 생성 유효성 검증 성공 테스트")
    @Test
    void validateCreate_success() {
        // given
        OrderTable orderTable = OrderTable.of(orderTableId, Empty.of(false));
        MenuProducts menuProducts = MenuProducts.of(Arrays.asList(MenuProduct.of(productId, Quantity.of(2))));
        Menu menu = Menu.of(Name.of("강정치킨_두마리_세트_메뉴"), Price.of(BigDecimal.valueOf(30_000)), menuGroupId, menuProducts);
        OrderLineItem orderLineItem = OrderLineItem.of(menuId, Quantity.of(1));
        Order order = Order.of(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.of(Arrays.asList(orderLineItem)));

        given(orderTableRepository.existsById(orderTableId)).willReturn(Boolean.TRUE);
        given(tableService.findById(orderTableId)).willReturn(orderTable);
        given(menuRepository.findByIdIn(Arrays.asList(menuId))).willReturn(Arrays.asList(menu));

        // when & then
        assertThatNoException()
                .isThrownBy(() -> orderValidator.validateCreate(order));
    }

    @DisplayName("주문 생성 유효성 검증 실패 테스트 - 주문 테이블 존재하지 않음")
    @Test
    void validateCreate_failure_validateOrderTable() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(menuId, Quantity.of(1));
        Order order = Order.of(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.of(Arrays.asList(orderLineItem)));

        given(orderTableRepository.existsById(orderTableId)).willReturn(Boolean.FALSE);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validateCreate(order));
    }

    @DisplayName("주문 생성 유효성 검증 성공 테스트 - 주문 테이블 비어있음")
    @Test
    void validateCreate_failure_validateOrderTableIsEmpty() {
        // given
        OrderTable orderTable = OrderTable.of(orderTableId, Empty.of(true));
        OrderLineItem orderLineItem = OrderLineItem.of(menuId, Quantity.of(1));
        Order order = Order.of(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.of(Arrays.asList(orderLineItem)));

        given(orderTableRepository.existsById(orderTableId)).willReturn(Boolean.TRUE);
        given(tableService.findById(orderTableId)).willReturn(orderTable);

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validateCreate(order));
    }

    @DisplayName("주문 생성 유효성 검증 실패 테스트 - 메뉴 수 일치하지 않음")
    @Test
    void validateCreate_failure_invalidMenuSize() {
        // given
        OrderTable orderTable = OrderTable.of(orderTableId, Empty.of(false));
        OrderLineItem orderLineItem = OrderLineItem.of(menuId, Quantity.of(1));
        Order order = Order.of(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.of(Arrays.asList(orderLineItem)));

        given(orderTableRepository.existsById(orderTableId)).willReturn(Boolean.TRUE);
        given(tableService.findById(orderTableId)).willReturn(orderTable);
        given(menuRepository.findByIdIn(Arrays.asList(menuId))).willReturn(Collections.emptyList());

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validateCreate(order));
    }

    @DisplayName("주문 상태 수정 유효성 검증 성공 테스트 - 주문 상태 COOKING")
    @Test
    void validateChangeOrderStatus_success_cooking() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(menuId, Quantity.of(1));
        Order order = Order.of(orderTableId, OrderStatus.COOKING, LocalDateTime.now(), OrderLineItems.of(Arrays.asList(orderLineItem)));

        // when & then
        assertThatNoException()
                .isThrownBy(() -> orderValidator.validateChangeOrderStatus(order));
    }

    @DisplayName("주문 상태 수정 유효성 검증 성공 테스트 - 주문 상태 MEAL")
    @Test
    void validateChangeOrderStatus_success_meal() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(menuId, Quantity.of(1));
        Order order = Order.of(orderTableId, OrderStatus.MEAL, LocalDateTime.now(), OrderLineItems.of(Arrays.asList(orderLineItem)));

        // when & then
        assertThatNoException()
                .isThrownBy(() -> orderValidator.validateChangeOrderStatus(order));
    }

    @DisplayName("주문 상태 수정 유효성 검증 실패 테스트 - 주문 상태가 COMPLETION인 경우 주문 상태를 수정할 수 없음")
    @Test
    void validateChangeOrderStatus_failure() {
        // given
        OrderLineItem orderLineItem = OrderLineItem.of(menuId, Quantity.of(1));
        Order order = Order.of(orderTableId, OrderStatus.COMPLETION, LocalDateTime.now(), OrderLineItems.of(Arrays.asList(orderLineItem)));

        // when & then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderValidator.validateChangeOrderStatus(order));
    }
}
