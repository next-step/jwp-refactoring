package kitchenpos.application.order;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.table.OrderTable;
import kitchenpos.domain.table.OrderTableRepository;
import kitchenpos.dto.order.OrderLineItemRequest;
import kitchenpos.dto.order.OrderRequest;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @InjectMocks
    private OrderValidator orderValidator;

    @Test
    @DisplayName("주문 상품이 없는 경우, 예외를 반환한다.")
    void createFail() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(null, "양념치킨", 19_000L, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, Lists.list(orderLineItemRequest));
        //when,then
        assertThatThrownBy(() -> {
            orderValidator.validate(orderRequest.toEntity());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 상품의 메뉴가 존재하지 않는 경우, 예외를 반환한다.")
    void createWithNoExistingMenu() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, "양념치킨", 19_000L, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, Lists.list(orderLineItemRequest));
        when(menuRepository.countByIdIn(any())).thenReturn(0);

        assertThatThrownBy(() -> {
            orderValidator.validate(orderRequest.toEntity());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않으면 예외를 반환한다.")
    void createWithNoExistingOrderTable() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, "양념치킨", 19_000L, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, Lists.list(orderLineItemRequest));
        when(menuRepository.countByIdIn(any())).thenReturn(1);
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderValidator.validate(orderRequest.toEntity());
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 빈 테이블이면 예외를 반환한다.")
    void createWithEmptyOrderTable() {
        OrderLineItemRequest orderLineItemRequest = new OrderLineItemRequest(1L, "양념치킨", 19_000L, 1L);
        OrderRequest orderRequest = new OrderRequest(1L, Lists.list(orderLineItemRequest));
        OrderTable orderTable = OrderTable.of(4, true);
        when(menuRepository.countByIdIn(any())).thenReturn(1);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(orderTable));

        assertThatThrownBy(() -> {
            orderValidator.validate(orderRequest.toEntity());
        }).isInstanceOf(IllegalArgumentException.class);
    }
}
