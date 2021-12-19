package kitchenpos.application;

import static kitchenpos.menu.MenuFixture.*;
import static kitchenpos.menugroup.MenuGroupFixture.*;
import static kitchenpos.order.OrderFixture.*;
import static kitchenpos.ordertable.OrderTableFixture.*;
import static kitchenpos.ordertablegroup.OrderTableGroupFixture.*;
import static kitchenpos.product.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import kitchenpos.IntegrationTest;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderStatus;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import kitchenpos.domain.TableGroup;

@DisplayName("주문 테이블 그룹 통합 테스트")
class TableGroupServiceTest extends IntegrationTest {
	@Autowired
	private TableGroupService tableGroupService;
	@Autowired
	private TableService tableService;
	@Autowired
	private ProductService productService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private MenuGroupService menuGroupService;
	@Autowired
	private OrderService orderService;

	@DisplayName("주문 테이블 그룹을 등록한다.")
	@Test
	void register() {
		// given
		OrderTable 빈_주문_테이블_1 = tableService.create(빈_주문_테이블_요청().toOrderTable());
		OrderTable 빈_주문_테이블_2 = tableService.create(빈_주문_테이블_요청().toOrderTable());

		// when
		TableGroup tableGroup = tableGroupService.create(
			주문_테이블_그룹(Arrays.asList(빈_주문_테이블_1.getId(), 빈_주문_테이블_2.getId())).toOrderTableGroup());

		// then
		List<Long> actualIds = tableGroup.getOrderTables().stream().map(OrderTable::getId).collect(Collectors.toList());
		List<Long> expectIds = Arrays.asList(빈_주문_테이블_1.getId(), 빈_주문_테이블_2.getId());

		assertAll(
			() -> assertThat(tableGroup).isNotNull(),
			() -> assertThat(tableGroup.getId()).isNotNull(),
			() -> assertThat(actualIds).isEqualTo(expectIds));
	}

	@DisplayName("주문 테이블 갯수가 2개 미만이면 등록할 수 없다.")
	@Test
	void registerFailOnLessThanTwo() {
		// given
		OrderTable 빈_주문_테이블_1 = tableService.create(빈_주문_테이블_요청().toOrderTable());

		// when
		ThrowingCallable throwingCallable = () ->
			tableGroupService.create(주문_테이블_그룹(Collections.singletonList(빈_주문_테이블_1.getId())).toOrderTableGroup());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 테이블이 등록되어 있지 않은 경우 등록할 수 없다.")
	@Test
	void registerFailOnNotFoundOrderTable() {
		// given
		OrderTable 빈_주문_테이블_1 = tableService.create(빈_주문_테이블_요청().toOrderTable());
		Long unknownOrderTableId = 0L;

		// when
		ThrowingCallable throwingCallable = () ->
			tableGroupService.create(
				주문_테이블_그룹(Arrays.asList(빈_주문_테이블_1.getId(), unknownOrderTableId)).toOrderTableGroup());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 테이블이 비어있지 않은 경우 등록할 수 없다.")
	@Test
	void registerFailOnNotEmptyOrderTable() {
		// given
		OrderTable 빈_주문_테이블 = tableService.create(빈_주문_테이블_요청().toOrderTable());
		OrderTable 비어있지않은_주문_테이블 = tableService.create(비어있지않은_주문_테이블_요청().toOrderTable());

		// when
		ThrowingCallable throwingCallable = () ->
			tableGroupService.create(
				주문_테이블_그룹(Arrays.asList(빈_주문_테이블.getId(), 비어있지않은_주문_테이블.getId())).toOrderTableGroup());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("이미 주문 테이블 그룹에 등록된 주문 테이블이 있는 경우 등록할 수 없다.")
	@Test
	void registerFailOnAlreadyBelongToOrderTableGroup() {
		// given
		OrderTable 빈_주문_테이블_1 = tableService.create(빈_주문_테이블_요청().toOrderTable());
		OrderTable 빈_주문_테이블_2 = tableService.create(빈_주문_테이블_요청().toOrderTable());
		OrderTable 빈_주문_테이블_3 = tableService.create(빈_주문_테이블_요청().toOrderTable());
		tableGroupService.create(주문_테이블_그룹(Arrays.asList(빈_주문_테이블_1.getId(), 빈_주문_테이블_2.getId())).toOrderTableGroup());

		// when
		ThrowingCallable throwingCallable = () ->
			tableGroupService.create(
				주문_테이블_그룹(Arrays.asList(빈_주문_테이블_1.getId(), 빈_주문_테이블_3.getId())).toOrderTableGroup());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 테이블 그룹을 해제할 수 있다.")
	@Test
	void ungroup() {
		// given
		OrderTable 빈_주문_테이블_1 = tableService.create(빈_주문_테이블_요청().toOrderTable());
		OrderTable 빈_주문_테이블_2 = tableService.create(빈_주문_테이블_요청().toOrderTable());
		TableGroup 주문_테이블_그룹 = tableGroupService.create(
			주문_테이블_그룹(Arrays.asList(빈_주문_테이블_1.getId(), 빈_주문_테이블_2.getId())).toOrderTableGroup());

		// when
		tableGroupService.ungroup(주문_테이블_그룹.getId());

		// then
	}

	@DisplayName("주문 테이블 그룹에 속한 주문 테이블들 중 완료되지 않은 주문이 있는 경우 주문 테이블 그룹을 해제할 수 없다.")
	@Test
	void ungroupFailOnNotCompletedOrderExist() {
		// given
		OrderTable 주문_테이블_1 = tableService.create(빈_주문_테이블_요청().toOrderTable());
		OrderTable 주문_테이블_2 = tableService.create(빈_주문_테이블_요청().toOrderTable());
		TableGroup 주문_테이블_그룹 = tableGroupService.create(
			주문_테이블_그룹(Arrays.asList(주문_테이블_1.getId(), 주문_테이블_2.getId())).toOrderTableGroup());
		Product 후라이드치킨_상품 = productService.create(후라이드치킨_상품_요청().toProduct());
		MenuGroup 추천_메뉴_그룹 = menuGroupService.create(추천_메뉴_그룹_요청().toMenuGroup());
		Menu 후라이드후라이드_메뉴 = menuService.create(후라이드후라이드_메뉴_요청(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId()).toMenu());
		Order 주문 = orderService.create(주문(주문_테이블_1.getId(), 후라이드후라이드_메뉴.getId(), 1).toOrder());
		orderService.changeOrderStatus(주문.getId(), 주문_상태(OrderStatus.MEAL).toOrder());

		// when
		ThrowingCallable throwingCallable = () -> tableGroupService.ungroup(주문_테이블_그룹.getId());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}
}
