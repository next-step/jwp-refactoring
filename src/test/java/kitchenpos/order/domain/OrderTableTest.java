package kitchenpos.order.domain;

import kitchenpos.exception.OrderErrorMessage;
import kitchenpos.exception.OrderTableErrorMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

@DisplayName("주문 테이블 단위 테스트")
public class OrderTableTest {
    private TableGroup 우아한_테이블;
    private OrderTable 우아한_주문_테이블_1;
    private OrderTable 우아한_주문_테이블_2;

    @BeforeEach
    void setUp() {
        우아한_테이블 = new TableGroup();
        우아한_주문_테이블_1 = new OrderTable(4, true);
        우아한_주문_테이블_2 = new OrderTable(3, true);

        우아한_테이블.group(Arrays.asList(우아한_주문_테이블_1, 우아한_주문_테이블_2));

        ReflectionTestUtils.setField(우아한_테이블, "id", 1L);
        ReflectionTestUtils.setField(우아한_주문_테이블_1, "id", 1L);
        ReflectionTestUtils.setField(우아한_주문_테이블_2, "id", 2L);
    }

    @DisplayName("주문 테이블이 비어있지 않으면 테이블 그룹을 생성할 수 없다.")
    @Test
    void 주문_테이블이_비어있지_않으면_테이블_그룹을_생성할_수_없다() {
        우아한_주문_테이블_1.ungroup();

        assertThatIllegalArgumentException()
                .isThrownBy(() -> 우아한_주문_테이블_1.checkOrderTableForTableGrouping())
                .withMessage(OrderTableErrorMessage.NON_EMPTY_ORDER_TABLE_CANNOT_BE_INCLUDED_IN_TABLE_GROUP.getMessage());
    }

    @DisplayName("테이블 그룹을 해제한다.")
    @Test
    void 테이블_그룹을_해제한다() {
        우아한_주문_테이블_1.ungroup();

        assertThat(우아한_주문_테이블_1.getTableGroup()).isNull();
    }

    @DisplayName("다른 테이블 그룹에 포함되어 있으면 빈 테이블로 변경할 수 없다.")
    @Test
    void 다른_테이블_그룹에_포함되어_있으면_빈_테이블로_변경할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 우아한_주문_테이블_1.changeEmpty(true, Collections.emptyList()))
                .withMessage(OrderTableErrorMessage.ALREADY_INCLUDED_IN_ANOTHER_TABLE_GROUP.getMessage());
    }

    @DisplayName("요리중이거나 식사중인 주문이 있다면 빈 테이블로 변경할 수 없다.")
    @ParameterizedTest
    @ValueSource(strings = {"COOKING", "MEAL"})
    void 요리중이거나_식사중인_주문이_있다면_빈_테이블로_변경할_수_없다(OrderStatus orderStatus) {
        OrderTable 주문_테이블 = new OrderTable(5, false);
        Order order = new Order(주문_테이블, orderStatus);

        assertThatIllegalArgumentException()
                .isThrownBy(() -> 주문_테이블.changeEmpty(true, Arrays.asList(order)))
                .withMessage(OrderErrorMessage.CANNOT_BE_CHANGED.getMessage());
    }

    @DisplayName("빈 테이블로 변경한다.")
    @Test
    void 빈_테이블로_변경() {
        OrderTable 주문_테이블 = new OrderTable(5, false);
        Order order = new Order(주문_테이블, OrderStatus.COMPLETION);

        주문_테이블.changeEmpty(true, Arrays.asList(order));

        assertThat(주문_테이블.isEmpty()).isTrue();
    }

    @DisplayName("입력된 주문 테이블 방문자 수가 음수이면 테이블 방문자 수를 변경할 수 없다.")
    @Test
    void 입력된_주문_테이블_방문자_수가_음수이면_테이블_방문자_수를_변경할_수_없다() {
        assertThatIllegalArgumentException()
                .isThrownBy(() -> 우아한_주문_테이블_1.changeNumberOfGuests(-1))
                .withMessage(OrderTableErrorMessage.INVALID_NUMBER_OF_GUESTS.getMessage());
    }

    @DisplayName("테이블 방문자 수 변경한다.")
    @Test
    void 테이블_방문자_수_변경한다() {
        우아한_주문_테이블_1.changeNumberOfGuests(3);

        assertThat(우아한_주문_테이블_1.getNumberOfGuests()).isEqualTo(3);
    }
}
