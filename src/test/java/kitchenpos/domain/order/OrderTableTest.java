package kitchenpos.domain.order;

import static kitchenpos.application.fixture.OrderFixture.요리중_주문_of;
import static kitchenpos.application.fixture.OrderTableFixture.단체지정된_주문테이블;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Collections;
import kitchenpos.common.exception.InvalidParameterException;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("OrderTable 클래스")
class OrderTableTest {

    @Test
    @DisplayName("`주문테이블`에 `단체 지정`이 되어 있으면 `빈 테이블` 여부를 변경 할 수 없다.")
    void 단체지정_중_빈테이블_변경_실패() {
        // given
        OrderTable 단체지정된_주문테이블 = 단체지정된_주문테이블();
        Orders orders = Orders.of(Collections.emptyList());

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 단체지정된_주문테이블.changeEmpty(orders, true);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("`주문 테이블`에서 `주문 항목`들의 `주문상태`가 `조리`,`식사`인 경우 `빈 테이블` 여부를 변경 할 수 없다.")
    void 결제완료_아닌_주문_빈테이블_변경_실패() {
        // given
        OrderTable 단체지정된_주문테이블 = 단체지정된_주문테이블();
        Orders orders = Orders.of(Collections.singletonList(요리중_주문_of(단체지정된_주문테이블)));

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 단체지정된_주문테이블.changeEmpty(orders, true);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("`주문 테이블`의 `방문한 손님 수`는 0명 이상이어야 변경할 수 있다.")
    void 방문손님수_마이너스_변경_실패() {
        // given
        OrderTable 단체지정된_주문테이블 = 단체지정된_주문테이블();

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 단체지정된_주문테이블.changeNumberOfGuests(-1);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }

    @Test
    @DisplayName("`주문 테이블`이 `빈 테이블`이 아니어야, 손님 수 를 변경할 수 있다.")
    void 주문테이블이_빈테이블_아니면_손님_수_변경_실패() {
        // given
        OrderTable 단체지정된_주문테이블 = 단체지정된_주문테이블();

        // when
        ThrowableAssert.ThrowingCallable actual = () -> 단체지정된_주문테이블.changeNumberOfGuests(1);

        // then
        assertThatThrownBy(actual).isInstanceOf(InvalidParameterException.class);
    }
}
