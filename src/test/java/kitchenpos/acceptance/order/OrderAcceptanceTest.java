package kitchenpos.acceptance.order;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.menu.MenuAcceptance;
import kitchenpos.acceptance.menugroup.MenuGroupAcceptance;
import kitchenpos.acceptance.ordertable.TableRestAcceptance;
import kitchenpos.acceptance.product.ProductAcceptance;
import kitchenpos.acceptance.tablegroup.TableGroupAcceptance;
import kitchenpos.menu.dto.MenuProductRequest;
import kitchenpos.menu.dto.MenuRequest;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.menugroup.dto.MenuGroupResponse;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.product.dto.ProductResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@DisplayName("주문 인수 테스트")
public class OrderAcceptanceTest extends OrderAcceptance {

	private ProductResponse 치킨;
	private ProductResponse 피자;
	private MenuGroupResponse menuGroupResponse;
	private List<MenuProductRequest> menuProduct1;
	private List<MenuProductRequest> menuProduct2;
	private MenuResponse menu1;
	private MenuResponse menu2;
	private OrderTableResponse orderTable1;
	private OrderTableResponse orderTable2;
	private OrderTableResponse orderTable3;
	private TableGroupResponse tableGroupResponse;
	private List<OrderLineItemRequest> orderLineItemRequests1;
	private List<OrderLineItemRequest> orderLineItemRequests2;

	@BeforeEach
	void init() {
		// 메뉴 만들기(상품, 메뉴그룹, 메뉴 생성)
		치킨 = ProductAcceptance.상품_등록되어_있음("치킨", 16000).as(ProductResponse.class);
		피자 = ProductAcceptance.상품_등록되어_있음("피자", 20000).as(ProductResponse.class);
		menuGroupResponse = MenuGroupAcceptance.메뉴_그룹_등록되어_있음("음식").as(MenuGroupResponse.class);
		menuProduct1 = Arrays.asList(MenuProductRequest.of(치킨.getId(), 2), MenuProductRequest.of(피자.getId(), 1));
		menuProduct2 = Arrays.asList(MenuProductRequest.of(치킨.getId(), 2), MenuProductRequest.of(피자.getId(), 2));

		MenuRequest request1 = MenuRequest.of("치피세트", 50000, menuGroupResponse.getId(), menuProduct1);
		MenuRequest request2 = MenuRequest.of("치피세트", 70000, menuGroupResponse.getId(), menuProduct2);

		menu1 = MenuAcceptance.메뉴_등록_요청(request1).as(MenuResponse.class);
		menu2 = MenuAcceptance.메뉴_등록_요청(request2).as(MenuResponse.class);

		// 테이블, 테이블 그룹 생성
		orderTable1 = TableRestAcceptance.주문_빈_테이블_등록되어있음().as(OrderTableResponse.class);
		orderTable2 = TableRestAcceptance.주문_빈_테이블_등록되어있음().as(OrderTableResponse.class);
		orderTable3 = TableRestAcceptance.주문_빈_테이블_등록되어있음().as(OrderTableResponse.class);
		tableGroupResponse = TableGroupAcceptance.단체_지정_등록_요청(
			TableGroupRequest.of(Arrays.asList(orderTable1.getId(), orderTable2.getId(), orderTable3.getId())))
			.as(TableGroupResponse.class);

		orderLineItemRequests1 = Arrays.asList(
			OrderLineItemRequest.of(menu1.getId(), 2),
			OrderLineItemRequest.of(menu2.getId(), 1));

		orderLineItemRequests2 = Collections.singletonList(OrderLineItemRequest.of(menu1.getId(), 3));
	}

	@DisplayName("주문 요청을 한다.")
	@Test()
	void createOrderTest() {
		// given
		OrderRequest request = OrderRequest.of(orderTable1.getId(), orderLineItemRequests1);

		// when
		ExtractableResponse<Response> response = 주문_등록_요청(request);

		// then
		주문_등록됨(request, response);
	}

	@DisplayName("주문 리스트를 조회 한다.")
	@Test
	void selectOrderListTest() {
		// given
		OrderRequest request1 = OrderRequest.of(orderTable1.getId(), orderLineItemRequests1);
		OrderRequest request2 = OrderRequest.of(orderTable2.getId(), orderLineItemRequests2);
		OrderResponse response1 = 주문_등록_요청(request1).as(OrderResponse.class);
		OrderResponse response2 = 주문_등록_요청(request2).as(OrderResponse.class);

		// when
		ExtractableResponse<Response> response = 메뉴_조회_요청();

		// then
		메뉴_목록_조회됨(response);
		메뉴_목록_포함됨(response, Arrays.asList(response1, response2));
	}

	@DisplayName("주문 상태 변경을 요청한다.")
	@Test
	void changeOrderStatusTest() {
		// given
		OrderResponse order = 주문_등록_요청(OrderRequest.of(orderTable1.getId(), orderLineItemRequests1)).as(
			OrderResponse.class);
		Map<String, String> param = new HashMap<>();
		param.put("orderStatus", OrderStatus.MEAL.name());

		// when
		ExtractableResponse<Response> response = 주문_상태변경_요청(order, param);

		// then
		주문_상태_변경됨(order, param, response);
	}

	@DisplayName("없는 주문 상태를 변경 요청한다.")
	@Test
	void errorOrderStatusTest() {
		// given
		OrderResponse order = 주문_등록_요청(OrderRequest.of(orderTable1.getId(), orderLineItemRequests1)).as(
			OrderResponse.class);
		Map<String, String> param = new HashMap<>();
		param.put("orderStatus", "complete");

		// when
		ExtractableResponse<Response> response = 주문_상태변경_요청(order, param);

		// then
		주문_상태_실패됨(response);
	}

}
