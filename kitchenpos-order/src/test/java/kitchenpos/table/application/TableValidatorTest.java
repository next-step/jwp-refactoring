package kitchenpos.table.application;

import kitchenpos.common.ErrorMessage;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderRepository;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.BDDMockito.given;

@DisplayName("주문과 주문 테이블 간의 validation 클래스 테스트")
@ExtendWith(MockitoExtension.class)
class TableValidatorTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private TableValidator tableValidator;

    private OrderTable 주문테이블;

    @BeforeEach
    void setUp() {
        주문테이블 = new OrderTable(2, false);

        ReflectionTestUtils.setField(주문테이블, "id", 1L);
    }

    @DisplayName("주문 테이블이 다른 테이블 그룹에 포함되어 있으면 빈 테이블로 변경할 수 없다.")
    @Test
    void 주문_테이블이_다른_테이블_그룹에_포함되어_있으면_빈_테이블로_변경할_수_없다() {
        OrderTable 주문테이블2 = new OrderTable(2, false);
        ReflectionTestUtils.setField(주문테이블2, "tableGroup", new TableGroup());

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableValidator.validateChangeEmpty(주문테이블2))
                .withMessage(ErrorMessage.ORDER_TABLE_ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
    }

    @DisplayName("조리중이거나 식사중인 주문이 있으면 빈 테이블로 변경할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 조리중이거나_식사중인_주문이_있으면_빈_테이블로_변경할_수_없다(OrderStatus orderStatus) {
        Order order = new Order(주문테이블.getId(), orderStatus);
        given(orderRepository.findByOrderTableId(주문테이블.getId())).willReturn(Arrays.asList(order));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableValidator.validateChangeEmpty(주문테이블))
                .withMessage(ErrorMessage.ORDER_CANNOT_BE_CHANGED.getMessage());
    }

    @DisplayName("조리중이거나 식사중인 주문이 있으면 테이블 그룹을 해제할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = { "COOKING", "MEAL" })
    void 조리중이거나_식사중인_주문이_있으면_테이블_그룹을_해제할_수_없다(OrderStatus orderStatus) {
        Order order = new Order(주문테이블.getId(), orderStatus);
        given(orderRepository.findByOrderTableIdIn(Arrays.asList(주문테이블.getId()))).willReturn(Arrays.asList(order));

        assertThatIllegalArgumentException()
                .isThrownBy(() -> tableValidator.validateUngroup(Arrays.asList(주문테이블.getId())))
                .withMessage(ErrorMessage.ORDER_CANNOT_BE_CHANGED.getMessage());
    }
}
