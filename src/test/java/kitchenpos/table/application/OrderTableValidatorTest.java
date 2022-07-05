package kitchenpos.table.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static kitchenpos.application.OrderServiceTest.orderedTime;
import static kitchenpos.application.TableGroupServiceTest.createOrderTableIds;
import static kitchenpos.domain.OrderLineItemsTest.createDuplicateOrderLineItems;
import static kitchenpos.domain.OrderLineItemsTest.createOrderLineItems;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderTableValidator orderTableValidator;

    @Test
    void 주문_항목이_비어있을_경우_등록할_수_없다() {
        // when & then
        assertThatThrownBy(() ->
                orderTableValidator.validateOrder(new Order(1L, null, orderedTime()))
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("주문 항목이 없습니다.");
    }

    @Test
    void 중복된_메뉴가_있을_경우_등록할_수_없다() {
        // given
        List<OrderLineItem> orderLineItems = createDuplicateOrderLineItems().elements();
        Order order = new Order(1L, orderLineItems, orderedTime());
        given(menuRepository.countByIdIn(Arrays.asList(1L, 1L))).willReturn(1L);

        // when & then
        assertThatThrownBy(() ->
                orderTableValidator.validateOrder(order)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 메뉴가 있습니다.");
    }

    @Test
    void 주문_테이블이_빈_테이블이면_등록할_수_없다() {
        // given
        Order order = new Order(1L, createOrderLineItems().elements(), orderedTime());
        given(menuRepository.countByIdIn(Arrays.asList(1L, 2L))).willReturn(2L);
        given(orderTableRepository.findById(1L)).willReturn(Optional.of(new OrderTable(1, true)));

        // when & then
        assertThatThrownBy(() ->
                orderTableValidator.validateOrder(order)
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("빈 테이블은 주문을 할 수 없습니다.");
    }

    @Test
    void 조리_또는_식사_중인_주문_테이블이_있을_경우_단체_지정을_해제할_수_없다() {
        // given
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(createOrderTableIds(), OrderStatus.findNotCompletionStatus()))
                .willReturn(true);

        // when & then
        assertThatThrownBy(() ->
                orderTableValidator.validateUngroup(createOrderTableIds())
        ).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("조리 또는 식사 중인 테이블은 단체 지정을 해제할 수 없습니다.");
    }
}
