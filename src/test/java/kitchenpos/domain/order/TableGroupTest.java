package kitchenpos.domain.order;


import static kitchenpos.application.fixture.OrderTableFixture.빈_테이블;
import static kitchenpos.application.fixture.TableGroupFixture.단체지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.common.exception.InvalidParameterException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("TableGroup 클래스")
class TableGroupTest {

    @Test
    @DisplayName("`단체 지정` 등록에 속한 `주문 테이블`은 모두 `빈 테이블` 아닌 상태로 변경 된다.")
    void 단체지정에_속한_테이블은_빈테이블이_아닌상태로_변경됨() {
        // given
        List<OrderTable> 주문테이블_목록 = Arrays.asList(빈_테이블(), 빈_테이블());

        // when
        TableGroup 단체지정 = 단체지정(주문테이블_목록);

        // then
        assertThat(단체지정.getOrderTables()).extracting("empty").contains(false, false);
    }

    @Test
    @DisplayName("`단체 지정`의 `주문 테이블`은 최소 2개이상 이어야 등록 할 수 있다.")
    void 주문테이블_2개미만_실패() {
        // when
        ThrowableAssert.ThrowingCallable actual = () -> OrderTables.of(
            Collections.singletonList(빈_테이블()));

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("`단체 지정`은 `주문 테이블`이 다른 `단체 지정`에 속해있지 않아야 등록 할 수 있다.")
    void 다른_단체지정에_포함된_주문테이블_단체지정_실패() {
        // given
        List<OrderTable> 주문테이블_목록 = Arrays.asList(빈_테이블(), 빈_테이블());
        TableGroup 단체지정 = 단체지정(주문테이블_목록);

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 단체지정(주문테이블_목록);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("`단체 지정`에 속한 `주문 테이블`들의 `주문상태`가 `조리`,`식사`인 경우 해지 할 수 없다.")
    void 결제완료가_아닌_주문테이블이_있는_단체지정은_해지_실패() {
        // given
        OrderTable 테이블_1 = OrderTable.of(0, true);
        OrderTable 테이블_2 = OrderTable.of(0, true);
        List<OrderTable> 주문테이블_목록 = Arrays.asList(테이블_1, 테이블_2);

        TableGroup 단체지정 = 단체지정(주문테이블_목록);

        OrderLineItem 주문항목 = OrderLineItem.of(null, 1L);
        Order 주문 = Order.of(테이블_1, Collections.singletonList(주문항목));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 단체지정.ungroup(
            Orders.of(Collections.singletonList(주문)));

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }
}
