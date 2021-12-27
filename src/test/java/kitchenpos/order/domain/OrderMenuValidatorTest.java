package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.exception.DuplicateMenuInOrderLineItems;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderMenuValidatorTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderMenuValidator orderMenuValidator;

    @DisplayName("중복되는 메뉴 ID가 존재해서는 안된다.")
    @Test
    void validateOrderLineItems1() {
        //given
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
            new OrderLineItemRequest(1L, 2),
            new OrderLineItemRequest(2L, 3),
            new OrderLineItemRequest(3L, 6),
            new OrderLineItemRequest(4L, 1),
            new OrderLineItemRequest(4L, 3));

        메뉴_개수_조회_모킹(orderLineItemRequests.size() - 1);

        //when, then
        assertThatThrownBy(
            () -> orderMenuValidator.validateOrderLineItems(orderLineItemRequests))
            .isInstanceOf(DuplicateMenuInOrderLineItems.class);
    }

    @DisplayName("없는 메뉴 ID가 존재해서는 안된다.")
    @Test
    void validateOrderLineItems2() {
        //given : 999999L 은 없는 메뉴라고 가정한다.
        List<OrderLineItemRequest> orderLineItemRequests = Arrays.asList(
            new OrderLineItemRequest(1L, 2),
            new OrderLineItemRequest(2L, 3),
            new OrderLineItemRequest(3L, 6),
            new OrderLineItemRequest(4L, 1),
            new OrderLineItemRequest(999999L, 3));

        메뉴_개수_조회_모킹(orderLineItemRequests.size() - 1);

        //when, then
        assertThatThrownBy(
            () -> orderMenuValidator.validateOrderLineItems(orderLineItemRequests))
            .isInstanceOf(DuplicateMenuInOrderLineItems.class);
    }

    private void 메뉴_개수_조회_모킹(long expectedResult) {
        given(menuRepository.countByIdIn(anyList()))
            .willReturn(expectedResult);
    }
}
