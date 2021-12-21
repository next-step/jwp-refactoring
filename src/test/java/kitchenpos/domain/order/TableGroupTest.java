package kitchenpos.domain.order;


import static kitchenpos.application.fixture.OrderFixture.식사중_주문_of;
import static kitchenpos.application.fixture.OrderTableFixture.빈_테이블;
import static kitchenpos.application.fixture.TableGroupFixture.단체지정;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.security.InvalidParameterException;
import java.util.Arrays;
import java.util.Collections;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;


@DisplayName("TableGroup 클래스")
class TableGroupTest {

    @Test
    @DisplayName("`단체 지정` 등록에 속한 `주문 테이블`은 모두 `빈 테이블` 아닌 상태로 변경 된다.")
    void 단체지정에_속한_테이블은_빈테이블이_아닌상태로_변경됨() {
        // given
        Orders 주문_목록 = Orders.of(Arrays.asList(식사중_주문_of(빈_테이블()), 식사중_주문_of(빈_테이블())));

        // when
        TableGroup 단체지정 = 단체지정(주문_목록.getOrderTables());

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
        OrderTables 주문_목록 = OrderTables.of(Arrays.asList(빈_테이블(), 빈_테이블()));
        TableGroup 단체지정 = 단체지정(주문_목록.getOrderTables());

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 단체지정(주문_목록.getOrderTables());

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("`단체 지정`에 속한 `주문 테이블`들의 `주문상태`가 `조리`,`식사`인 경우 해지 할 수 없다.")
    void 결제완료가_아닌_주문테이블이_있는_단체지정은_해지_실패() {
        // given
        Orders 주문_목록 = Orders.of(Arrays.asList(식사중_주문_of(빈_테이블()), 식사중_주문_of(빈_테이블())));
        TableGroup 단체지정 = 단체지정(주문_목록.getOrderTables());

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 단체지정.ungroup(주문_목록);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }
}
