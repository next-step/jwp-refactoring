package kitchenpos.order;

import static kitchenpos.table.TableAcceptanceTest.빈자리;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.dao.MenuRepository;
import kitchenpos.order.application.OrderService;
import kitchenpos.order.dao.OrderLineItemRepository;
import kitchenpos.order.dao.OrderRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItems;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dao.OrderTableRepository;
import kitchenpos.table.domain.OrderTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @InjectMocks
    OrderService orderService;

    @Mock
    MenuRepository menuRepository;

    @Mock
    OrderRepository orderRepository;

    @Mock
    OrderLineItemRepository orderLineItemRepository;

    @Mock
    OrderTableRepository orderTableRepository;

    Order 주문;
    OrderTable 테이블1;
    OrderLineItem 주문내역;
    OrderRequest 주문요청;

    @BeforeEach
    void setUp() {
        테이블1 = new OrderTable(5, false);
        주문내역 = new OrderLineItem(1L, 1L);

        주문 = new Order(테이블1);

        주문.saveOrderLineItems(new OrderLineItems(Collections.singletonList(주문내역)));
        주문내역.saveOrder(주문);

        주문요청 = new OrderRequest(1L,
                Collections.singletonList(new OrderLineItemRequest(주문내역.getMenuId(), 주문내역.getQuantity())));
    }

    @Test
    @DisplayName("주문을 저장한다")
    void create() {
        // given
        given(menuRepository.countByIdIn(any())).willReturn(1L);
        given(orderTableRepository.findById(any())).willReturn(Optional.of(테이블1));
        given(orderRepository.save(any())).willReturn(주문);
        given(orderLineItemRepository.saveAll(any())).willReturn(Collections.singletonList(주문내역));

        // when
        OrderResponse actual = orderService.create(주문요청);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getOrderStatus()).isEqualTo(OrderStatus.COOKING)
        );
    }

    @Test
    @DisplayName("주문시 주문내역이 존재해야 한다")
    void create_EmptyOrderLineItemsError() {
        // given
        주문요청 = new OrderRequest(1L, Collections.emptyList());
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(테이블1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.create(주문요청)
        ).withMessageContaining("주문 내역이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주문시 주문내역의 메뉴는 모두 존재하는 메뉴여야 한다")
    void create_nonMenuError() {
        // given
        OrderLineItemRequest 주문내역1 = new OrderLineItemRequest(1L, 1L);
        OrderLineItemRequest 주문내역2 = new OrderLineItemRequest(1L, 1L);
        주문요청 = new OrderRequest(1L, Arrays.asList(주문내역1, 주문내역2));

        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(테이블1));
        given(menuRepository.countByIdIn(any())).willReturn(1L);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.create(주문요청)
        ).withMessageContaining("존재하지 않는 메뉴입니다.");
    }

    @Test
    @DisplayName("주문시 주문테이블 정보를 가지고 있어야 한다")
    void create_nonExistTableError() {
        // given
        테이블1.setEmpty(빈자리);
        given(orderTableRepository.findById(any())).willReturn(Optional.ofNullable(테이블1));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.create(주문요청)
        ).withMessageContaining("주문 테이블이 존재하지 않습니다.");
    }

    @Test
    @DisplayName("주문 리스트를 조회한다")
    void list() {
        // given
        given(orderRepository.findAll()).willReturn(Collections.singletonList(주문));

        // when
        List<OrderResponse> actual = orderService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(1),
                () -> assertThat(actual.get(0).getOrderLineItems()).hasSize(1)
        );
    }

    @ParameterizedTest(name = "주문 상태를 {0}에서 {1}로 변경한다")
    @CsvSource(value = {"COOKING|MEAL", "COOKING|COMPLETION", "MEAL|COMPLETION"}, delimiter = '|')
    void changeOrderStatus(OrderStatus currentStatus, OrderStatus expected) {
        // given
        주문.changeOrderStatus(currentStatus);
        given(orderRepository.findById(any())).willReturn(Optional.of(주문));

        // when
        OrderResponse actual = orderService.changeOrderStatus(1L, expected);

        // then
        assertThat(actual.getOrderStatus()).isEqualTo(expected);
    }

    @Test
    @DisplayName("현재 주문 상태가 계산 완료인 경우 변경할 수 없다")
    void changeOrderStatus_completion() {
        // given
        주문.changeOrderStatus(OrderStatus.COMPLETION);
        given(orderRepository.findById(any())).willReturn(Optional.of(주문));

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> orderService.changeOrderStatus(1L, OrderStatus.COMPLETION)
        ).withMessageContaining("주문 상태가 계산 완료인 경우 변경할 수 없습니다.");
    }
}
