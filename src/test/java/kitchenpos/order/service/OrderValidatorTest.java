package kitchenpos.order.service;

import kitchenpos.common.exception.IllegalArgumentException;
import kitchenpos.common.exception.NotFoundException;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderValidator;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.BDDMockito.given;

@DisplayName("주문 검증 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {
    @InjectMocks
    private OrderValidator orderValidator;

    @Mock
    private OrderTableRepository orderTableRepository;

    @Mock
    private MenuRepository menuRepository;

    @DisplayName("주문 테이블을 미존재 예외")
    @Test
    void 주문_테이블_미존재_검증() {
        OrderRequest orderRequest = new OrderRequest(55L, null, Collections.singletonList(new OrderLineItemRequest(1L, 1L)));
        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> orderValidator.validate(orderRequest.toOrder()));

        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessage("해당 주문 테이블을 찾을 수 없습니다.");
    }

    @DisplayName("주문 라인 1개 미만 예외")
    @Test
    void 주문_라인_1개미만_검증() {
        OrderRequest orderRequest = new OrderRequest(55L, null, new ArrayList<>());

        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willReturn(Optional.of(OrderTable.of(10, false)));

        Throwable thrown = catchThrowable(() -> orderValidator.validate(orderRequest.toOrder()));

        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessage("주문 라인은 최소 1개 이상 필요합니다.");
    }

    @DisplayName("메뉴 미존재 예외")
    @Test
    void 메뉴_미존재_검증() {
        OrderRequest orderRequest = new OrderRequest(55L, null, Collections.singletonList(new OrderLineItemRequest(1L, 1L)));
        given(orderTableRepository.findById(orderRequest.getOrderTableId())).willReturn(Optional.of(OrderTable.of(10, false)));
        given(menuRepository.findById(1L)).willReturn(Optional.empty());

        Throwable thrown = catchThrowable(() -> orderValidator.validate(orderRequest.toOrder()));

        assertThat(thrown).isInstanceOf(NotFoundException.class)
                .hasMessage("해당 메뉴를 찾을 수 없습니다.");
    }

    @DisplayName("완료된 주문의 상태 변경 요청 예외")
    @Test
    void 완료된_주문_상태_변경_요청_검증() {
        OrderRequest orderRequest = new OrderRequest(55L, null, Collections.singletonList(new OrderLineItemRequest(1L, 1L)));
        Order order = orderRequest.toOrder();
        order.changeOrderStatus(orderValidator, OrderStatus.COMPLETION);

        Throwable thrown = catchThrowable(() -> orderValidator.validateChangeable(order));

        assertThat(thrown).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("완료된 주문의 상태는 변경할 수 없습니다.");
    }
}
