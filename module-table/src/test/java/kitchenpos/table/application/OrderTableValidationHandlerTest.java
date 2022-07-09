package kitchenpos.table.application;

import static kitchenpos.OrderLineItemTestFixture.주문_항목_생성;
import static kitchenpos.OrderTestFixture.빈_주문_생성;
import static kitchenpos.OrderTestFixture.주문_생성;
import static kitchenpos.table.__fixture__.OrderTableTestFixture.주문_테이블_생성;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import kitchenpos.menu.infra.MenuRepository;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.domain.OrderTableValidationEvent;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.infra.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("OrderTableValidator 테스트")
@ExtendWith(MockitoExtension.class)
public class OrderTableValidationHandlerTest {
    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;
    @InjectMocks
    private OrderTableValidationHandler orderTableValidationHandler;
    private OrderLineItem 주문_항목;
    private Order 주문;

    @BeforeEach
    void setUp() {
        주문_항목 = 주문_항목_생성(1L, "후라이드치킨", BigDecimal.valueOf(16_000), 1L);
        주문 = 주문_생성(1L, OrderStatus.COOKING, LocalDateTime.now(), Arrays.asList(주문_항목));
    }

    @Test
    @DisplayName("주문 시 주문 항목이 비어있으면 Exception")
    public void createEmptyException() {
        final Order 빈_주문 = 빈_주문_생성(2L, OrderStatus.COOKING, LocalDateTime.now());

        assertThatThrownBy(
                () -> orderTableValidationHandler.validateOrder(new OrderTableValidationEvent(빈_주문))).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 시 주문 항목이 메뉴에 존재하지 않으면 Exception")
    public void createOrderLineItemsNotExistsException() {
        given(menuRepository.countByIdIn(any(List.class))).willReturn(2L);
        assertThatThrownBy(
                () -> orderTableValidationHandler.validateOrder(new OrderTableValidationEvent(주문))).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 존재하지 않을 경우 Exception")
    public void createTableNotExistsException() {
        given(menuRepository.countByIdIn(any(List.class))).willReturn(1L);
        given(orderTableRepository.findById(주문.getOrderTableId())).willReturn(Optional.empty());

        assertThatThrownBy(
                () -> orderTableValidationHandler.validateOrder(new OrderTableValidationEvent(주문))).isInstanceOf(
                IllegalArgumentException.class);
    }

    @Test
    @DisplayName("주문 테이블이 비어있을 경우 Exception")
    public void createEmptyTableException() {
        final OrderTable 주문_테이블 = 주문_테이블_생성(1L, null, 4, true);

        given(menuRepository.countByIdIn(any(List.class))).willReturn(1L);
        given(orderTableRepository.findById(주문.getOrderTableId())).willReturn(Optional.of(주문_테이블));

        assertThatThrownBy(
                () -> orderTableValidationHandler.validateOrder(new OrderTableValidationEvent(주문))).isInstanceOf(
                IllegalArgumentException.class);
    }
}
