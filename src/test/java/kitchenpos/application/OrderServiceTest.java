package kitchenpos.application;

import static kitchenpos.menu.MenuFixture.*;
import static kitchenpos.menugroup.MenuGroupFixture.*;
import static kitchenpos.order.OrderFixture.*;
import static kitchenpos.ordertable.OrderTableFixture.*;
import static kitchenpos.product.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.ThrowableAssert.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

@DisplayName("주문 통합 테스트")
class OrderServiceTest extends IntegrationTest {
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

	@DisplayName("주문을 등록한다.")
	@Test
	void register() {
		// given
		OrderTable 비어있지않은_주문_테이블 = tableService.create(비어있지않은_주문_테이블().toOrderTable());
		Product 후라이드치킨_상품 = productService.create(후라이드치킨_상품_요청().toProduct());
		MenuGroup 추천_메뉴_그룹 = menuGroupService.create(추천_메뉴_그룹_요청().toMenuGroup());
		Menu 후라이드후라이드_메뉴 = menuService.create(후라이드후라이드_메뉴(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId()).toMenu());

		// when
		Order order = orderService.create(주문(비어있지않은_주문_테이블.getId(), 후라이드후라이드_메뉴.getId(), 1).toOrder());

		// then
		assertAll(
			() -> assertThat(order).isNotNull(),
			() -> assertThat(order.getId()).isNotNull());
	}

	@DisplayName("주문 항목이 없는 경우 주문을 등록할 수 없다.")
	@Test
	void registerFailOnEmptyOrderLineItem() {
		// given
		OrderTable 비어있지않은_주문_테이블 = tableService.create(비어있지않은_주문_테이블().toOrderTable());

		// when
		ThrowingCallable throwingCallable = () ->
			orderService.create(주문_항목_없는_주문(비어있지않은_주문_테이블.getId()).toOrder());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 항목이 등록되지 않은 메뉴라면 주문을 등록할 수 없다.")
	@Test
	void registerFailOnNotFoundMenu() {
		// given
		OrderTable 비어있지않은_주문_테이블 = tableService.create(비어있지않은_주문_테이블().toOrderTable());
		Long unknownMenuId = 0L;

		// when
		ThrowingCallable throwingCallable = () ->
			orderService.create(주문(비어있지않은_주문_테이블.getId(), unknownMenuId, 1).toOrder());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 테이블이 비어있다면 주문을 등록할 수 없다.")
	@Test
	void registerFailOnEmptyOrderTable() {
		// given
		OrderTable 빈_주문_테이블 = tableService.create(빈_주문_테이블().toOrderTable());
		Product 후라이드치킨_상품 = productService.create(후라이드치킨_상품_요청().toProduct());
		MenuGroup 추천_메뉴_그룹 = menuGroupService.create(추천_메뉴_그룹_요청().toMenuGroup());
		Menu 후라이드후라이드_메뉴 = menuService.create(후라이드후라이드_메뉴(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId()).toMenu());

		// when
		ThrowingCallable throwingCallable = () ->
			orderService.create(주문(빈_주문_테이블.getId(), 후라이드후라이드_메뉴.getId(), 1).toOrder());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}

	@DisplayName("주문 목록을 조회할 수 있다.")
	@Test
	void findAll() {
		// given
		OrderTable 비어있지않은_주문_테이블_1 = tableService.create(비어있지않은_주문_테이블().toOrderTable());
		OrderTable 비어있지않은_주문_테이블_2 = tableService.create(비어있지않은_주문_테이블().toOrderTable());
		Product 후라이드치킨_상품 = productService.create(후라이드치킨_상품_요청().toProduct());
		MenuGroup 추천_메뉴_그룹 = menuGroupService.create(추천_메뉴_그룹_요청().toMenuGroup());
		Menu 후라이드후라이드_메뉴 = menuService.create(후라이드후라이드_메뉴(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId()).toMenu());
		Order 주문_1 = orderService.create(주문(비어있지않은_주문_테이블_1.getId(), 후라이드후라이드_메뉴.getId(), 1).toOrder());
		Order 주문_2 = orderService.create(주문(비어있지않은_주문_테이블_2.getId(), 후라이드후라이드_메뉴.getId(), 2).toOrder());

		// when
		List<Order> orders = orderService.list();

		// then
		List<Long> actualIds = orders.stream()
			.map(Order::getId)
			.collect(Collectors.toList());
		List<Long> expectedIds = Stream.of(주문_1, 주문_1)
			.map(Order::getId)
			.collect(Collectors.toList());
		assertThat(actualIds).containsAll(expectedIds);
	}

	@DisplayName("주문 상태를 변경할 수 있다.")
	@Test
	void changeOrderStatus() {
		// given
		OrderTable 비어있지않은_주문_테이블 = tableService.create(비어있지않은_주문_테이블().toOrderTable());
		Product 후라이드치킨_상품 = productService.create(후라이드치킨_상품_요청().toProduct());
		MenuGroup 추천_메뉴_그룹 = menuGroupService.create(추천_메뉴_그룹_요청().toMenuGroup());
		Menu 후라이드후라이드_메뉴 = menuService.create(후라이드후라이드_메뉴(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId()).toMenu());
		Order 주문 = orderService.create(주문(비어있지않은_주문_테이블.getId(), 후라이드후라이드_메뉴.getId(), 1).toOrder());
		OrderStatus orderStatus = OrderStatus.MEAL;

		// when
		Order order = orderService.changeOrderStatus(주문.getId(), 주문_상태(orderStatus).toOrder());

		// then
		assertThat(order.getOrderStatus()).isEqualTo(orderStatus.name());
	}

	@DisplayName("주문 상태가 완료라면 변경할 수 없다.")
	@Test
	void changeOrderStatusFailOnCompleted() {
		// given
		OrderTable 비어있지않은_주문_테이블 = tableService.create(비어있지않은_주문_테이블().toOrderTable());
		Product 후라이드치킨_상품 = productService.create(후라이드치킨_상품_요청().toProduct());
		MenuGroup 추천_메뉴_그룹 = menuGroupService.create(추천_메뉴_그룹_요청().toMenuGroup());
		Menu 후라이드후라이드_메뉴 = menuService.create(후라이드후라이드_메뉴(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId()).toMenu());
		Order 주문 = orderService.create(주문(비어있지않은_주문_테이블.getId(), 후라이드후라이드_메뉴.getId(), 1).toOrder());
		orderService.changeOrderStatus(주문.getId(), 주문_상태(OrderStatus.COMPLETION).toOrder());

		// when
		ThrowingCallable throwingCallable = () ->
			orderService.changeOrderStatus(주문.getId(), 주문_상태(OrderStatus.MEAL).toOrder());

		// then
		assertThatExceptionOfType(RuntimeException.class).isThrownBy(throwingCallable);
	}
}
