package kitchenpos.order.acceptance;

import java.util.Collections;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.acceptance.menu.MenuAcceptanceTestStep;
import kitchenpos.menu.acceptance.menu.MenuFixture;
import kitchenpos.menu.acceptance.menugroup.MenuGroupAcceptanceTestStep;
import kitchenpos.menu.acceptance.menugroup.MenuGroupFixture;
import kitchenpos.menu.product.ProductAcceptanceTestStep;
import kitchenpos.menu.product.ProductFixture;
import kitchenpos.menu.ui.dto.MenuGroupResponse;
import kitchenpos.menu.ui.dto.MenuResponse;
import kitchenpos.menu.ui.dto.ProductResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.dto.OrderResponse;
import kitchenpos.table.acceptance.ordertable.OrderTableAcceptanceTestStep;
import kitchenpos.table.acceptance.ordertable.OrderTableFixture;
import kitchenpos.table.ui.dto.OrderTableResponse;

@DisplayName("주문 관련 기능")
class OrderAcceptanceTest extends AcceptanceTest {

	OrderAcceptanceTestStep step = new OrderAcceptanceTestStep();
	ProductAcceptanceTestStep products = new ProductAcceptanceTestStep();
	MenuGroupAcceptanceTestStep menuGroups = new MenuGroupAcceptanceTestStep();
	MenuAcceptanceTestStep menus = new MenuAcceptanceTestStep();
	OrderTableAcceptanceTestStep orderTables = new OrderTableAcceptanceTestStep();

	OrderTableResponse 주문_테이블;
	List<ProductResponse> 상품목록;
	MenuGroupResponse 메뉴그룹;
	MenuResponse 메뉴;

	/**
	 * Feature: 주문 관리 기능
	 * Background
	 * Given 상품을 등록되어 있음
	 * Given 메뉴 그룹 등록되어 있음
	 * Given 메뉴를 등록되어 있음
	 * Given 주문 테이블 등록되어 있음
	 */
	@BeforeEach
	void setup() {
		상품목록 = products.등록되어_있음(ProductFixture.상품목록(3));
		메뉴그룹 = menuGroups.등록되어_있음(MenuGroupFixture.메뉴그룹());
		메뉴 = menus.등록되어_있음(MenuFixture.메뉴(상품목록, 메뉴그룹));
		주문_테이블 = orderTables.등록되어_있음(OrderTableFixture.주문_테이블());
	}

	/**
	 * Scenario: 주문 관리
	 * When 주문 등록을 요청하면
	 * Then 주문 등록에 성공한다
	 * Then 주문이 조리 상태로 설정된다.
	 * When 주문 상태를 '식사'로 변경을 요청할 경우
	 * Then 주문 상태 변경에 성공한다.
	 */
	@Test
	void 주문_관리() {
		// when
		ExtractableResponse<Response> 등록_요청_응답 =
			step.등록_요청(OrderFixture.주문(주문_테이블, Lists.newArrayList(메뉴)));
		// then
		OrderResponse 주문 = step.등록됨(등록_요청_응답);
		// then
		step.주문_상턔_확인(주문, OrderStatus.COOKING);
		// when
		OrderStatus 식사중 = OrderStatus.MEAL;
		ExtractableResponse<Response> 주문_변경_요청_응답 = step.주문_상태_변경_요청(주문.getId(), 식사중);
		// then
		step.수정됨(주문_변경_요청_응답);
	}

	/**
	 * When 주문 항목이 없을 경우
	 * Then 주문 등록에 실패한다
	 */
	@Test
	void 주문_항목이_없음() {
		ExtractableResponse<Response> 등록_요청_응답 =
			step.등록_요청(OrderFixture.주문(주문_테이블, Collections.emptyList()));

		step.등록_실패함(등록_요청_응답);
	}

	/**
	 * When 메뉴가 존재하지 않을 경우
	 * Then 주문 등록에 실패한다
	 */
	@Test
	void 메뉴가_존재하지_않음() {
		MenuResponse 존재하지_않는_메뉴 = MenuFixture.메뉴(-1L, 메뉴그룹);
		ExtractableResponse<Response> 등록_요청_응답 =
			step.등록_요청(OrderFixture.주문(주문_테이블, Lists.newArrayList(존재하지_않는_메뉴)));

		step.등록_실패함(등록_요청_응답);
	}

	/**
	 * When 주문 테이블이 존재하지 않을 경우
	 * Then 주문 등록에 실패한다
	 */
	@Test
	void 주문_테이블이_존재하지_않음() {
		OrderTableResponse 존재하지_않는_주문_테이블 = OrderTableFixture.주문_테이블(1L);
		ExtractableResponse<Response> 등록_요청_응답 =
			step.등록_요청(OrderFixture.주문(존재하지_않는_주문_테이블, Lists.newArrayList(메뉴)));

		step.등록_실패함(등록_요청_응답);
	}

	/**
	 * Given 주문의 상태를 계산 완료로 변경하고
	 * When 계산 완료된 주문의 상태를 변경 요청할 경우
	 * Then 주문 상태 변경에 실패한다.
	 */
	@Test
	void 계산_완료_주문의_상태_변경() {
		OrderResponse 주문 = step.주문_등록됨(주문_테이블, Lists.newArrayList(메뉴));
		// given
		step.주문_상태_변경_요청(주문.getId(), OrderStatus.COMPLETION);

		// when
		ExtractableResponse<Response> 변경_요청_응답 = step.주문_상태_변경_요청(주문.getId(), OrderStatus.COOKING);
		// then
		step.수정_실패함(변경_요청_응답);
	}

	/**
	 * When 비어 있지 않은 주문 테이블에 주문을 등록할 경우
	 * Then 주문 등록에 실패한다
	 */
	@Test
	void 비어_있지_않은_주문_테이블에_주문_등록() {
		// given
		OrderTableResponse 비어_있지_않은_주문_테이블 = orderTables.등록되어_있음(OrderTableFixture.주문_테이블(5, false));

		// when
		ExtractableResponse<Response> 등록_응답 = step.등록_요청(OrderFixture.주문(비어_있지_않은_주문_테이블, Lists.newArrayList(메뉴)));

		// then
		step.등록_실패함(등록_응답);
	}

	/**
	 * Scenario: 주문 등록시 주문 테이블의 주문가능 상태 변경
	 * 	When 주문을 등록하면
	 * 	Then 주문 테이블이 주문 불가 상태로 변경된다
	 *  When 주문의 상태를 완료로 변경하면
	 *  Then 주문 테이블의 주문 가능 상태로 변경된다
	 */
	@Test
	void 주문_등록시_테이블이_주문불가_상태로_변경() {
		// when
		OrderResponse 주문 = step.등록되어_있음(OrderFixture.주문(주문_테이블, Lists.newArrayList(메뉴)));

		boolean 주문_가능_상태 = false;
		List<OrderTableResponse> 실제_주문_테이블 = orderTables.목록_조회();
		// then
		step.주문_테이블_상태_변경됨(주문_테이블.getId(), 주문_가능_상태, 실제_주문_테이블);

		// when
		step.주문_상태_변경_요청(주문.getId(), OrderStatus.COMPLETION);

		// then
		주문_가능_상태 = true;
		실제_주문_테이블 = orderTables.목록_조회();
		step.주문_테이블_상태_변경됨(주문_테이블.getId(), 주문_가능_상태, 실제_주문_테이블);
	}
}

