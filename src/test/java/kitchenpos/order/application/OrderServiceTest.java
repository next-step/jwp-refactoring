package kitchenpos.order.application;


import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.Optional;
import kitchenpos.menu.application.MenuService;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("주문 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private MenuService menuService;

    @InjectMocks
    private OrderService orderService;

    @DisplayName("주문 테이블이 비어있는 주문은 예외가 발생한다.")
    @Test
    void createOrderEmptyTable() {
        OrderTable 빈_테이블 = new OrderTable(0, true);
        OrderLineItemRequest 주문항목_요청 = new OrderLineItemRequest(1L, 1L);
        OrderRequest 주문_요청 = new OrderRequest(1L, OrderStatus.COOKING, Arrays.asList(주문항목_요청));

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(빈_테이블));

        assertThatThrownBy(() -> orderService.create(주문_요청))
            .isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("주문 항목에 없는 메뉴가 있는 주문은 예외가 발생한다.")
    @Test
    void createOrderNotRegisteredMenu() {
        OrderTable 주문테이블 = new OrderTable(3, false);
        OrderLineItemRequest 주문항목_요청 = new OrderLineItemRequest(1L, 1L);
        OrderRequest 주문_요청 = new OrderRequest(1L, OrderStatus.COOKING, Arrays.asList(주문항목_요청));

        given(orderTableRepository.findById(anyLong())).willReturn(Optional.of(주문테이블));
        given(menuService.findById(anyLong())).willThrow(IllegalArgumentException.class);

        assertThatThrownBy(() -> orderService.create(주문_요청))
            .isInstanceOf(IllegalArgumentException.class);
    }
}