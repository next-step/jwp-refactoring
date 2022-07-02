package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatNoException;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import java.util.Optional;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.exception.CannotMakeOrderException;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.OrderTableRepository;
import kitchenpos.table.exception.NotExistTableException;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderCreationValidatorTest {

    private OrderCreationValidator orderCreationValidator;

    @Mock
    private MenuRepository menuRepository;

    @Mock
    private OrderTableRepository orderTableRepository;

    @BeforeEach
    void setUp() {
        orderCreationValidator = new OrderCreationValidator(menuRepository, orderTableRepository);
    }

    @Test
    @DisplayName("외부로부터 전달받은 메뉴 ID를 가진 메뉴들이 실제로 존재하는지 확인")
    void 메뉴존재여부확인() {
        when(menuRepository.countByIdIn(Lists.newArrayList(1L, 2L))).thenReturn(2L);
        Order order = new Order(1L, Lists.newArrayList(new OrderLineItem(1L, 1), new OrderLineItem(2L, 2)));
        OrderLineItems orderLineItems = order.getOrderLineItems();

        assertThatNoException()
                .isThrownBy(() -> orderCreationValidator.validateAllMenusExist(orderLineItems));
    }

    @Test
    @DisplayName("외부로부터 전달받은 메뉴 ID중 유효하지 않은 ID가 있는 경우 예외 발생")
    void 메뉴존재여부확인_실패() {
        when(menuRepository.countByIdIn(Lists.newArrayList(1L, 2L))).thenReturn(0L);
        Order order = new Order(1L, Lists.newArrayList(new OrderLineItem(1L, 1), new OrderLineItem(2L, 2)));
        OrderLineItems orderLineItems = order.getOrderLineItems();

        assertThatThrownBy(() -> orderCreationValidator.validateAllMenusExist(orderLineItems))
                .isInstanceOf(CannotMakeOrderException.class);
    }

    @Test
    @DisplayName("테이블이 주문을 받을수 있는 상태인지 확인")
    void 테이블_주문가능여부() {
        Long orderTableId = 1L;
        when(orderTableRepository.findById(orderTableId))
                .thenReturn(Optional.of(new OrderTable(4, false)));

        assertThatNoException()
                .isThrownBy(() -> orderCreationValidator.validateTableToMakeOrder(orderTableId));
    }

    @Test
    @DisplayName("테이블이 존재하지 않는 경우 주문 불가")
    void 테이블_주문불가능케이스_테이블이_없는경우() {
        Long orderTableId = 1L;
        when(orderTableRepository.findById(orderTableId))
                .thenReturn(Optional.empty());

        assertThatThrownBy(() -> orderCreationValidator.validateTableToMakeOrder(orderTableId))
                .isInstanceOf(NotExistTableException.class);
    }

    @Test
    @DisplayName("빈 테이블인 경우 주문 불가")
    void 테이블_주문불가능케이스_빈테이블() {
        Long orderTableId = 1L;
        when(orderTableRepository.findById(orderTableId))
                .thenReturn(Optional.of(new OrderTable(0, true)));

        assertThatThrownBy(() -> orderCreationValidator.validateTableToMakeOrder(orderTableId))
                .isInstanceOf(CannotMakeOrderException.class);
    }
}
