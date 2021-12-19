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

@DisplayName("주문 테이블 통합 테스트")
class TableServiceTest extends IntegrationTest {
	@Autowired
	private TableService tableService;
	@Autowired
	private TableGroupService tableGroupService;
	@Autowired
	private ProductService productService;
	@Autowired
	private MenuService menuService;
	@Autowired
	private MenuGroupService menuGroupService;
	@Autowired
	private OrderService orderService;

	@DisplayName("주문 테이블을 등록한다.")
	@Test
	void create() {
		// given
		OrderTable request = 빈_주문_테이블().toOrderTable();

		// when
		OrderTable orderTable = tableService.create(request);

		// then
		assertAll(
			() -> assertThat(orderTable).isNotNull(),
			() -> assertThat(orderTable.getId()).isNotNull(),
			() -> assertThat(orderTable.getNumberOfGuests()).isEqualTo(request.getNumberOfGuests()),
			() -> assertThat(orderTable.isEmpty()).isEqualTo(request.isEmpty()));
	}

	@DisplayName("주문 테이블 목록을 조회할 수 있다.")
	@Test
	void list() {
		// given
		OrderTable 빈_주문_테이블 = tableService.create(빈_주문_테이블().toOrderTable());
		OrderTable 비어있지않은_주문_테이블 = tableService.create(비어있지않은_주문_테이블().toOrderTable());

		// when
		List<OrderTable> orderTables = tableService.list();

		// then
		List<Long> actualIds = orderTables.stream().map(OrderTable::getId).collect(Collectors.toList());
		List<Long> expectIds = Arrays.asList(빈_주문_테이블.getId(), 비어있지않은_주문_테이블.getId());
		assertThat(actualIds).containsAll(expectIds);
	}

	@DisplayName("주문 테이블의 빈 상태를 변경할 수 있다.")
	@Test
	void changEmpty() {
		// given
		OrderTable 빈_주문_테이블 = tableService.create(빈_주문_테이블().toOrderTable());

		// when
		OrderTable orderTable = tableService.changeEmpty(빈_주문_테이블.getId(), 비우기().toOrderTable());

		// then
		assertThat(orderTable.isEmpty()).isEqualTo(비우기().isEmpty());
	}

	@DisplayName("주문 테이블 그룹에 속해 있는 경우 주문 테이블의 빈 상태를 변경할 수 없다.")
	@Test
	void changEmptyFailOnBelongToOrderTableGroup() {
		// given
		OrderTable 빈_주문_테이블_1 = tableService.create(빈_주문_테이블().toOrderTable());
		OrderTable 빈_주문_테이블_2 = tableService.create(빈_주문_테이블().toOrderTable());
		TableGroup 주문_테이블_그룹 = tableGroupService.create(
			주문_테이블_그룹(Arrays.asList(빈_주문_테이블_1.getId(), 빈_주문_테이블_2.getId())).toOrderTableGroup());

		// when
		ThrowingCallable throwingCallable = () ->
			tableService.changeEmpty(빈_주문_테이블_1.getId(), 채우기().toOrderTable());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 테이블에 완료되지 않은 주문이 있는 경우 주문 테이블의 빈 상태를 변경할 수 없다.")
	@Test
	void changEmptyFailOnNotCompletedOrderExist() {
		// given
		OrderTable 비어있지않은_주문_테이블 = tableService.create(비어있지않은_주문_테이블().toOrderTable());
		Product 후라이드치킨_상품 = productService.create(후라이드치킨_상품_요청().toProduct());
		MenuGroup 추천_메뉴_그룹 = menuGroupService.create(추천_메뉴_그룹_요청().toMenuGroup());
		Menu 후라이드후라이드_메뉴 = menuService.create(후라이드후라이드_메뉴_요청(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId()).toMenu());
		Order 주문 = orderService.create(주문(비어있지않은_주문_테이블.getId(), 후라이드후라이드_메뉴.getId(), 1).toOrder());
		orderService.changeOrderStatus(주문.getId(), 주문_상태(OrderStatus.MEAL).toOrder());

		// when
		ThrowingCallable throwingCallable = () ->
			tableService.changeEmpty(비어있지않은_주문_테이블.getId(), 비우기().toOrderTable());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 테이블에 손님 수를 변경할 수 있다")
	@Test
	void changeNumberOfGuests() {
		// given
		OrderTable 비어있지않은_주문_테이블 = tableService.create(비어있지않은_주문_테이블().toOrderTable());
		int numberOfGuests = 6;

		// when
		OrderTable orderTable = tableService.changeNumberOfGuests(
			비어있지않은_주문_테이블.getId(), 손님_수_변경(numberOfGuests).toOrderTable());

		// then
		assertThat(orderTable.getNumberOfGuests()).isEqualTo(numberOfGuests);
	}

	@DisplayName("손님 수가 0보다 작은 경우 주문 테이블에 손님 수를 변경할 수 없다")
	@Test
	void changeNumberOfGuestsFailOnNegative() {
		// given
		OrderTable 비어있지않은_주문_테이블 = tableService.create(비어있지않은_주문_테이블().toOrderTable());
		int numberOfGuests = -1;

		// when
		ThrowingCallable throwingCallable = () ->
			tableService.changeNumberOfGuests(비어있지않은_주문_테이블.getId(), 손님_수_변경(numberOfGuests).toOrderTable());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("빈 주문 테이블인 경우 주문 테이블에 손님 수를 변경할 수 없다")
	@Test
	void changeNumberOfGuestsFailOnEmpty() {
		// given
		OrderTable 빈_주문_테이블 = tableService.create(빈_주문_테이블().toOrderTable());
		int numberOfGuests = 6;

		// when
		ThrowingCallable throwingCallable = () ->
			tableService.changeNumberOfGuests(빈_주문_테이블.getId(), 손님_수_변경(numberOfGuests).toOrderTable());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}
}
