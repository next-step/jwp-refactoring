package kitchenpos.application;

import static kitchenpos.ordertable.OrderTableFixture.*;
import static kitchenpos.ordertablegroup.OrderTableGroupFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.OrderDao;
import kitchenpos.dao.OrderTableDao;
import kitchenpos.dao.TableGroupDao;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("주문 테이블 그룹 단위 테스트")
@ExtendWith(MockitoExtension.class)
class TableGroupServiceTest {
	@Mock
	private OrderDao orderDao;
	@Mock
	private OrderTableDao orderTableDao;
	@Mock
	private TableGroupDao tableGroupDao;
	@InjectMocks
	private TableGroupService tableGroupService;

	@DisplayName("주문 테이블 그룹을 등록한다.")
	@Test
	void register() {
		// given
		given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(빈_주문_테이블_1(), 빈_주문_테이블_2()));
		given(tableGroupDao.save(any())).willReturn(주문_테이블_그룹());
		given(orderTableDao.save(any())).willReturn(any());

		// when
		TableGroup tableGroup = tableGroupService.create(
			주문_테이블_그룹_요청(Arrays.asList(빈_주문_테이블_1().getId(), 빈_주문_테이블_2().getId())).toOrderTableGroup());

		// then
		List<Long> actualIds = tableGroup.getOrderTables()
			.stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());
		List<Long> expectIds = Stream.of(빈_주문_테이블_1(), 빈_주문_테이블_2())
			.map(OrderTable::getId)
			.collect(Collectors.toList());
		assertAll(
			() -> assertThat(tableGroup).isNotNull(),
			() -> assertThat(tableGroup.getId()).isNotNull(),
			() -> assertThat(actualIds).isEqualTo(expectIds));
	}

	@DisplayName("주문 테이블 갯수가 2개 미만이면 등록할 수 없다.")
	@Test
	void registerFailOnLessThanTwo() {
		// given

		// when
		ThrowingCallable throwingCallable = () ->
			tableGroupService.create(주문_테이블_그룹_요청(Collections.singletonList(빈_주문_테이블_1().getId())).toOrderTableGroup());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 테이블이 등록되어 있지 않은 경우 등록할 수 없다.")
	@Test
	void registerFailOnNotFoundOrderTable() {
		// given
		given(orderTableDao.findAllByIdIn(any())).willReturn(Collections.singletonList(빈_주문_테이블_1()));
		Long unknownOrderTableId = 0L;

		// when
		ThrowingCallable throwingCallable = () ->
			tableGroupService.create(
				주문_테이블_그룹_요청(Arrays.asList(빈_주문_테이블_1().getId(), unknownOrderTableId)).toOrderTableGroup());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 테이블이 비어있지 않은 경우 등록할 수 없다.")
	@Test
	void registerFailOnNotEmptyOrderTable() {
		// given
		given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(빈_주문_테이블(), 비어있지않은_주문_테이블()));

		// when
		ThrowingCallable throwingCallable = () ->
			tableGroupService.create(
				주문_테이블_그룹_요청(Arrays.asList(빈_주문_테이블().getId(), 비어있지않은_주문_테이블().getId())).toOrderTableGroup());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("이미 주문 테이블 그룹에 등록된 주문 테이블이 있는 경우 등록할 수 없다.")
	@Test
	void registerFailOnAlreadyBelongToOrderTableGroup() {
		// given
		given(orderTableDao.findAllByIdIn(any())).willReturn(Arrays.asList(빈_주문_테이블_1_그룹핑됨(), 빈_주문_테이블_2_그룹핑됨()));

		// when
		ThrowingCallable throwingCallable = () ->
			tableGroupService.create(
				주문_테이블_그룹_요청(Arrays.asList(빈_주문_테이블_1().getId(), 빈_주문_테이블_2().getId())).toOrderTableGroup());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 테이블 그룹을 해제할 수 있다.")
	@Test
	void ungroup() {
		// given
		given(orderTableDao.findAllByTableGroupId(any())).willReturn(
			Arrays.asList(빈_주문_테이블_1_그룹핑됨(), 빈_주문_테이블_2_그룹핑됨()));
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(false);
		given(orderTableDao.save(any())).willReturn(any());

		// when
		tableGroupService.ungroup(주문_테이블_그룹().getId());

		// then
	}

	@DisplayName("주문 테이블 그룹에 속한 주문 테이블들 중 완료되지 않은 주문이 있는 경우 주문 테이블 그룹을 해제할 수 없다.")
	@Test
	void ungroupFailOnNotCompletedOrderExist() {
		// given
		given(orderTableDao.findAllByTableGroupId(any())).willReturn(
			Arrays.asList(빈_주문_테이블_1_그룹핑됨(), 빈_주문_테이블_2_그룹핑됨()));
		given(orderDao.existsByOrderTableIdInAndOrderStatusIn(any(), any())).willReturn(true);

		// when
		ThrowingCallable throwingCallable = () -> tableGroupService.ungroup(주문_테이블_그룹().getId());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}
}
