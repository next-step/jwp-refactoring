package kitchenpos.orderTable.validator;

import static kitchenpos.order.domain.OrderStatus.COOKING;
import static kitchenpos.order.domain.OrderStatus.MEAL;
import static kitchenpos.tableGroup.domain.TableGroup.ORDER_TABLE_REQUEST_MIN;
import static kitchenpos.utils.DomainFixtureFactory.createOrderTable;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.orderTable.domain.OrderTable;
import kitchenpos.orderTable.domain.OrderTables;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderTableValidatorTest {
    @Mock
    private OrderRepository orderRepository;
    @InjectMocks
    private OrderTableValidator orderTableValidator;
    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = createOrderTable(1L, 2, false);
    }

    @DisplayName("주문테이블의 주문이 완료상태가 아닌경우 테스트")
    @Test
    void validateComplete() {
        given(orderRepository.existsByOrderTableIdAndOrderStatusIn(주문테이블.id(), Arrays.asList(COOKING, MEAL))).willReturn(true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTableValidator.validateComplete(주문테이블.id()))
                .withMessage("주문테이블의 주문이 완료상태가 아닙니다.");
    }

    @DisplayName("주문테이블의 주문이 완료상태가 아닌경우 테스트")
    @Test
    void validateComplete2() {
        given(orderRepository.existsByOrderTableIdInAndOrderStatusIn(Lists.newArrayList(주문테이블.id()),
                Arrays.asList(COOKING, MEAL))).willReturn(true);
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTableValidator.validateComplete(Lists.newArrayList(주문테이블.id())))
                .withMessage("주문테이블들의 주문이 완료상태가 아닙니다.");
    }

    @DisplayName("주문테이블 Id 갯수가 2 미만인 경우 테스트")
    @Test
    void validateReserveEvent() {
        assertThatIllegalArgumentException()
                .isThrownBy(
                        () -> orderTableValidator.validateReserveEvent(OrderTables.from(Lists.newArrayList(주문테이블)), Lists.newArrayList(1L)))
                .withMessage(ORDER_TABLE_REQUEST_MIN + "이상 주문테이블이 필요합니다.");
    }

    @DisplayName("주문테이블 Id가 빈 경우 테스트")
    @Test
    void validateReserveEventWithOrderTableIdNull() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTableValidator.validateReserveEvent(OrderTables.from(Lists.newArrayList(주문테이블)), Lists.emptyList()))
                .withMessage(ORDER_TABLE_REQUEST_MIN + "이상 주문테이블이 필요합니다.");
    }

    @DisplayName("주문테이블 수와 주문테이블 Id 갯수가 단체지정의 주문테이블 수가 일치하지 않는 경우 테스트")
    @Test
    void validateReserveEventWithNotEqualOrderTableSize() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> orderTableValidator.validateReserveEvent(OrderTables.from(Lists.newArrayList(주문테이블)), Lists.newArrayList(1L, 2L, 3L)))
                .withMessage("비교하는 수와 주문 테이블의 수가 일치하지 않습니다.");
    }
}
