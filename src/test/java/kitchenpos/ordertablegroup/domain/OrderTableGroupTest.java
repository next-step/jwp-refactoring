package kitchenpos.ordertablegroup.domain;

import static kitchenpos.ordertable.OrderTableFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문 테이블 그룹")
class OrderTableGroupTest {

	@DisplayName("그룹 생성")
	@Test
	void group() {
		// given
		OrderTableGroup orderTableGroup = OrderTableGroup.newInstance();

		// when
		orderTableGroup.group(Arrays.asList(
				빈_주문_테이블_3번().getId(),
				빈_주문_테이블_4번().getId()),
			new ValidOrderTableGroupValidator());

		// then
		assertThat(orderTableGroup).isNotNull();
	}

	@DisplayName("그룹 생성 실패 - 주문 테이블이 2개 미만인 경우")
	@Test
	void groupFailOnLessThenTwo() {
		// given
		OrderTableGroup orderTableGroup = OrderTableGroup.newInstance();

		// when
		ThrowingCallable throwingCallable = () -> orderTableGroup.group(Collections.singletonList(
				빈_주문_테이블_3번().getId()),
			new CountInvalidOrderTableGroupValidator());

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("생성 실패 - 이미 주문 테이블 그룹이 있는 경우")
	@Test
	void groupFailOnAlreadyHavingOrderTableGroup() {
		// given
		OrderTableGroup orderTableGroup = OrderTableGroup.newInstance();

		// when
		ThrowingCallable throwingCallable = () -> orderTableGroup.group(Arrays.asList(
				빈_주문_테이블_3번().getId(),
				그룹핑된_주문_테이블_5번().getId()),
			new AlreadyGroupedOrderTableGroupValidator());

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("생성 실패 - 주문 테이블이 비어있지 않은 경우")
	@Test
	void groupFailOnNotEmpty() {
		// given
		OrderTableGroup orderTableGroup = OrderTableGroup.newInstance();

		// when
		ThrowingCallable throwingCallable = () -> orderTableGroup.group(Arrays.asList(
				비어있지않은_주문_테이블_1번().getId(),
				빈_주문_테이블_3번().getId()),
			new NotEmptyOrderTableGroupValidator());

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
	}

	@DisplayName("그룹 해제")
	@Test
	void ungroup() {
		// given
		OrderTableGroup orderTableGroup = OrderTableGroup.newInstance();
		orderTableGroup.group(Arrays.asList(
				빈_주문_테이블_3번().getId(),
				빈_주문_테이블_4번().getId()),
			new ValidOrderTableGroupValidator());

		// when
		orderTableGroup.ungroup(new ValidOrderTableGroupValidator());

		// then
	}

	@DisplayName("그룹 해제 실패 - 주문 테이블에 완료되지 않은 주문이 있는 경우")
	@Test
	void ungroupFailOnHavingNotCompletedOrder() {
		// given
		OrderTableGroup orderTableGroup = OrderTableGroup.newInstance();
		orderTableGroup.group(Arrays.asList(
				빈_주문_테이블_3번().getId(),
				빈_주문_테이블_4번().getId()),
			new ValidOrderTableGroupValidator());

		// when
		ThrowingCallable throwingCallable = () -> orderTableGroup.ungroup(
			new NotCompletedOrderExistOrderTableGroupValidator());

		// then
		assertThatThrownBy(throwingCallable).isInstanceOf(IllegalStateException.class);
	}
}
