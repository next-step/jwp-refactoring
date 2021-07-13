package kitchenpos.ordering.application;

import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.ordering.domain.OrderLineItem;
import kitchenpos.ordering.domain.OrderStatus;
import kitchenpos.ordering.domain.OrderValidator;
import kitchenpos.ordering.domain.Ordering;
import kitchenpos.ordering.dto.OrderRequest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class OrderValidatorTest {

    private OrderValidator orderValidator;

    @Mock
    private MenuRepository menuRepository;
    @Mock
    private OrderTableRepository orderTableRepository;

    private Ordering order;

    private Long order1Id = 1L;
    private Long order1OrderTableId = 1L;
    private OrderStatus order1OrderStatus = OrderStatus.COOKING;
    private LocalDateTime order1OrderTime = LocalDateTime.now();
    private OrderLineItem orderLineItem = new OrderLineItem(1L, 1L, 1);
    private List<OrderLineItem> order1OrderLineItems = Arrays.asList(orderLineItem);

    @BeforeEach
    void setUp() {
        orderValidator = new OrderValidator(menuRepository, orderTableRepository);

        OrderRequest orderRequest1 = new OrderRequest(order1OrderTableId, order1OrderLineItems);
        order = orderRequest1.toEntity();
    }

    @DisplayName("검증 - 주문에 주문항목이 없으면 등록할 수 없다.")
    @Test
    void 주문의_주문항목이_올바르지_않으면_안된다_1() {
        List<OrderLineItem> emptyOrderLineItems = Arrays.asList();
        OrderRequest orderRequest1 = new OrderRequest(order1OrderTableId, emptyOrderLineItems);
        order = orderRequest1.toEntity();

        assertThatThrownBy(() -> {
            orderValidator.validate(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("검증 - 주문에 있는 주문항목들은 모두 등록되어 있어야 한다.")
    @Test
    void 주문의_주문항목이_올바르지_않으면_안된다() {
        long falseCount = 2;
        when(menuRepository.countByIdIn(any())).thenReturn(falseCount);

        assertThatThrownBy(() -> {
            orderValidator.validate(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("검증 - 주문의 주문테이블이 주문테이블로 등록 안되어 있으면 등록할 수 없다.")
    @Test
    void 주문의_주문테이블이_올바르지_않으면_안된다_1() {
        when(menuRepository.countByIdIn(any())).thenReturn(1L);
        when(orderTableRepository.findById(any())).thenReturn(Optional.empty());

        assertThatThrownBy(() -> {
            orderValidator.validate(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("검증 - 주문의 주문테이블이 비어있으면 주문할 수 없다.")
    @Test
    void 주문의_주문테이블이_올바르지_않으면_안된다_2() {
        OrderTable falseOrderTable = new OrderTable(1L, 0, true);

        when(menuRepository.countByIdIn(any())).thenReturn(1L);
        when(orderTableRepository.findById(any())).thenReturn(Optional.of(falseOrderTable));

        assertThatThrownBy(() -> {
            orderValidator.validate(order);
        }).isInstanceOf(IllegalArgumentException.class);
    }

}
