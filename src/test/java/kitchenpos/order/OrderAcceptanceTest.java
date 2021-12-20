package kitchenpos.order;

import static kitchenpos.menu.MenuAcceptanceTest.*;
import static kitchenpos.menu.MenuFixture.*;
import static kitchenpos.menugroup.MenuGroupAcceptanceTest.*;
import static kitchenpos.menugroup.MenuGroupFixture.*;
import static kitchenpos.order.OrderFixture.*;
import static kitchenpos.ordertable.OrderTableAcceptanceTest.*;
import static kitchenpos.ordertable.OrderTableFixture.*;
import static kitchenpos.product.ProductAcceptanceTest.*;
import static kitchenpos.product.ProductFixture.*;
import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderStatus;
import kitchenpos.menu.dto.MenuDto;
import kitchenpos.menugroup.dto.MenuGroupDto;
import kitchenpos.order.dto.OrderDto;
import kitchenpos.order.dto.OrderRequest;
import kitchenpos.ordertable.dto.OrderTableDto;
import kitchenpos.product.dto.ProductDto;

public class OrderAcceptanceTest extends AcceptanceTest {

	private static ExtractableResponse<Response> 주문_등록_요청(OrderRequest request) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().post("/api/orders")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 주문_등록되어_있음(OrderRequest request) {
		return 주문_등록_요청(request);
	}

	private static ExtractableResponse<Response> 주문_상태_변경_요청(Long orderId, OrderStatus orderStatus) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderRequest(orderStatus))
			.when().put("/api/orders/{orderId}/order-status", orderId)
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 주문_상태_변경되어_있음(Long orderId, OrderStatus orderStatus) {
		return 주문_상태_변경_요청(orderId, orderStatus);
	}

	@DisplayName("주문을 등록한다.")
	@Test
	void register() {
		// given
		OrderTableDto 비어있지않은_주문_테이블 = 주문_테이블_등록되어_있음(비어있지않은_주문_테이블_요청()).as(OrderTableDto.class);
		ProductDto 후라이드치킨_상품 = 상품_등록되어_있음(후라이드치킨_상품_요청()).as(ProductDto.class);
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);
		MenuDto 후라이드후라이드_메뉴 = 메뉴_등록되어_있음(후라이드후라이드_메뉴_요청(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId())).as(MenuDto.class);

		// when
		ExtractableResponse<Response> response = 주문_등록_요청(주문_요청(비어있지않은_주문_테이블.getId(), 후라이드후라이드_메뉴.getId(), 1));

		// then
		주문_등록됨(response);
		주문_상태_일치함(response, OrderStatus.COOKING);
	}

	@DisplayName("주문 항목이 없는 경우 주문을 등록할 수 없다.")
	@Test
	void registerFailOnEmptyOrderLineItem() {
		// given
		OrderTableDto 비어있지않은_주문_테이블 = 주문_테이블_등록되어_있음(비어있지않은_주문_테이블_요청()).as(OrderTableDto.class);

		// when
		ExtractableResponse<Response> response = 주문_등록_요청(주문_항목_없는_주문_요청(비어있지않은_주문_테이블.getId()));

		// then
		주문_등록되지_않음(response);
	}

	@DisplayName("주문 항목이 등록되지 않은 메뉴라면 주문을 등록할 수 없다.")
	@Test
	void registerFailOnNotFoundMenu() {
		// given
		OrderTableDto 비어있지않은_주문_테이블 = 주문_테이블_등록되어_있음(비어있지않은_주문_테이블_요청()).as(OrderTableDto.class);
		Long unknownMenuId = 0L;

		// when
		ExtractableResponse<Response> response = 주문_등록_요청(주문_요청(비어있지않은_주문_테이블.getId(), unknownMenuId, 1));

		// then
		주문_등록되지_않음(response);
	}

	@DisplayName("주문 테이블이 비어있다면 주문을 등록할 수 없다.")
	@Test
	void registerFailOnEmptyOrderTable() {
		// given
		OrderTableDto 빈_주문_테이블 = 주문_테이블_등록되어_있음(빈_주문_테이블_요청()).as(OrderTableDto.class);
		ProductDto 후라이드치킨_상품 = 상품_등록되어_있음(후라이드치킨_상품_요청()).as(ProductDto.class);
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);
		MenuDto 후라이드후라이드_메뉴 = 메뉴_등록되어_있음(후라이드후라이드_메뉴_요청(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId())).as(MenuDto.class);

		// when
		ExtractableResponse<Response> response = 주문_등록_요청(주문_요청(빈_주문_테이블.getId(), 후라이드후라이드_메뉴.getId(), 1));

		// then
		주문_등록되지_않음(response);
	}

	@DisplayName("주문 목록을 조회할 수 있다.")
	@Test
	void findAll() {
		// given
		OrderTableDto 비어있지않은_주문_테이블_1 = 주문_테이블_등록되어_있음(비어있지않은_주문_테이블_요청()).as(OrderTableDto.class);
		OrderTableDto 비어있지않은_주문_테이블_2 = 주문_테이블_등록되어_있음(비어있지않은_주문_테이블_요청()).as(OrderTableDto.class);
		ProductDto 후라이드치킨_상품 = 상품_등록되어_있음(후라이드치킨_상품_요청()).as(ProductDto.class);
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);
		MenuDto 후라이드후라이드_메뉴 = 메뉴_등록되어_있음(후라이드후라이드_메뉴_요청(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId())).as(MenuDto.class);
		OrderDto 주문_1 = 주문_등록되어_있음(주문_요청(비어있지않은_주문_테이블_1.getId(), 후라이드후라이드_메뉴.getId(), 1)).as(OrderDto.class);
		OrderDto 주문_2 = 주문_등록되어_있음(주문_요청(비어있지않은_주문_테이블_2.getId(), 후라이드후라이드_메뉴.getId(), 2)).as(OrderDto.class);

		// when
		ExtractableResponse<Response> response = 주문_목록_조회_요청();

		// then
		주문_목록_응답됨(response);
		주문_목록_포함됨(response, Arrays.asList(주문_1, 주문_2));
	}

	@DisplayName("주문 상태를 변경할 수 있다.")
	@Test
	void changeOrderStatus() {
		// given
		OrderTableDto 비어있지않은_주문_테이블 = 주문_테이블_등록되어_있음(비어있지않은_주문_테이블_요청()).as(OrderTableDto.class);
		ProductDto 후라이드치킨_상품 = 상품_등록되어_있음(후라이드치킨_상품_요청()).as(ProductDto.class);
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);
		MenuDto 후라이드후라이드_메뉴 = 메뉴_등록되어_있음(후라이드후라이드_메뉴_요청(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId())).as(MenuDto.class);
		OrderDto 주문 = 주문_등록되어_있음(주문_요청(비어있지않은_주문_테이블.getId(), 후라이드후라이드_메뉴.getId(), 1)).as(OrderDto.class);

		// when
		ExtractableResponse<Response> response = 주문_상태_변경_요청(주문.getId(), OrderStatus.MEAL);

		// then
		주문_상태_변경됨(response);
		주문_상태_일치함(response, OrderStatus.MEAL);
	}

	@DisplayName("주문 상태가 완료라면 변경할 수 없다.")
	@Test
	void changeOrderStatusFailOnCompleted() {
		// given
		OrderTableDto 비어있지않은_주문_테이블 = 주문_테이블_등록되어_있음(비어있지않은_주문_테이블_요청()).as(OrderTableDto.class);
		ProductDto 후라이드치킨_상품 = 상품_등록되어_있음(후라이드치킨_상품_요청()).as(ProductDto.class);
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);
		MenuDto 후라이드후라이드_메뉴 = 메뉴_등록되어_있음(후라이드후라이드_메뉴_요청(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId())).as(MenuDto.class);
		OrderDto 주문 = 주문_등록되어_있음(주문_요청(비어있지않은_주문_테이블.getId(), 후라이드후라이드_메뉴.getId(), 1)).as(OrderDto.class);
		주문_상태_변경되어_있음(주문.getId(), OrderStatus.COMPLETION);

		// when
		ExtractableResponse<Response> response = 주문_상태_변경_요청(주문.getId(), OrderStatus.MEAL);

		// then
		주문_상태_변경되지_않음(response);
	}

	private void 주문_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
		assertThat(response.as(OrderDto.class).getId()).isNotNull();
	}

	private void 주문_등록되지_않음(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	private ExtractableResponse<Response> 주문_목록_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get("/api/orders")
			.then().log().all()
			.extract();
	}

	private void 주문_목록_응답됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 주문_목록_포함됨(ExtractableResponse<Response> response, List<OrderDto> expectedOrders) {
		List<Long> actualIds = response.jsonPath().getList(".", OrderDto.class).stream()
			.map(OrderDto::getId)
			.collect(Collectors.toList());

		List<Long> expectedIds = expectedOrders.stream()
			.map(OrderDto::getId)
			.collect(Collectors.toList());

		assertThat(actualIds).isEqualTo(expectedIds);
	}

	private void 주문_상태_변경됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 주문_상태_일치함(ExtractableResponse<Response> response, OrderStatus expectedOrderStatus) {
		OrderDto actual = response.as(OrderDto.class);
		assertThat(actual.getOrderStatus()).isEqualTo(expectedOrderStatus);
	}

	private void 주문_상태_변경되지_않음(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
