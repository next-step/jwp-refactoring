package kitchenpos.ordertable;

import static kitchenpos.menu.MenuAcceptanceTest.*;
import static kitchenpos.menu.MenuFixture.*;
import static kitchenpos.menugroup.MenuGroupAcceptanceTest.*;
import static kitchenpos.menugroup.MenuGroupFixture.*;
import static kitchenpos.order.OrderAcceptanceTest.*;
import static kitchenpos.order.OrderFixture.*;
import static kitchenpos.ordertable.OrderTableFixture.*;
import static kitchenpos.ordertablegroup.OrderTableGroupAcceptanceTest.*;
import static kitchenpos.ordertablegroup.OrderTableGroupFixture.*;
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
import kitchenpos.ordertable.dto.OrderTableDto;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.product.dto.ProductDto;

@DisplayName("주문 테이블 인수 테스트")
public class OrderTableAcceptanceTest extends AcceptanceTest {

	private static ExtractableResponse<Response> 주문_테이블_등록_요청(OrderTableRequest request) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(request)
			.when().post("/api/tables")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 주문_테이블_등록되어_있음(OrderTableRequest request) {
		return 주문_테이블_등록_요청(request);
	}

	@DisplayName("주문 테이블을 등록한다.")
	@Test
	void register() {
		// when
		ExtractableResponse<Response> response = 주문_테이블_등록_요청(빈_주문_테이블());

		// then
		주문_테이블_등록됨(response);
	}

	@DisplayName("주문 테이블 목록을 조회할 수 있다.")
	@Test
	void findAll() {
		// given
		OrderTableDto 빈_주문_테이블 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		OrderTableDto 비어있지않은_주문_테이블 = 주문_테이블_등록되어_있음(비어있지않은_주문_테이블()).as(OrderTableDto.class);

		// when
		ExtractableResponse<Response> response = 주문_테이블_목록_조회_요청();

		// then
		주문_테이블_목록_응답됨(response);
		주문_테이블_목록_포함됨(response, Arrays.asList(빈_주문_테이블, 비어있지않은_주문_테이블));
	}

	@DisplayName("주문 테이블의 빈 상태를 변경할 수 있다.")
	@Test
	void changEmpty() {
		// given
		OrderTableDto 빈_주문_테이블 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		boolean empty = false;

		// when
		ExtractableResponse<Response> response = 주문_테이블_빈_상태_변경_요청(빈_주문_테이블.getId(), empty);

		// then
		주문_테이블_빈_상태_변경됨(response);
		주문_테이블_빈_상태_일치함(response, empty);
	}

	@DisplayName("주문 테이블 그룹에 속해 있는 경우 주문 테이블의 빈 상태를 변경할 수 없다.")
	@Test
	void changEmptyFailOnBelongToOrderTableGroup() {
		// given
		OrderTableDto 빈_주문_테이블_1 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		OrderTableDto 빈_주문_테이블_2 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		주문_테이블_그룹_등록되어_있음(주문_테이블_그룹(Arrays.asList(빈_주문_테이블_1.getId(), 빈_주문_테이블_2.getId())));

		// when
		ExtractableResponse<Response> response = 주문_테이블_빈_상태_변경_요청(빈_주문_테이블_1.getId(), false);

		// then
		주문_테이블_빈_상태_변경되지_않음(response);
	}

	@DisplayName("주문 테이블에 완료되지 않은 주문이 있는 경우 주문 테이블의 빈 상태를 변경할 수 없다.")
	@Test
	void changEmptyFailOnNotCompletedOrderExist() {
		// given
		OrderTableDto 비어있지않은_주문_테이블 = 주문_테이블_등록되어_있음(비어있지않은_주문_테이블()).as(OrderTableDto.class);
		ProductDto 후라이드치킨_상품 = 상품_등록되어_있음(후라이드치킨_상품_요청()).as(ProductDto.class);
		MenuGroupDto 추천_메뉴_그룹 = 메뉴_그룹_등록되어_있음(추천_메뉴_그룹_요청()).as(MenuGroupDto.class);
		MenuDto 후라이드후라이드_메뉴 = 메뉴_등록되어_있음(후라이드후라이드_메뉴_요청(추천_메뉴_그룹.getId(), 후라이드치킨_상품.getId())).as(MenuDto.class);
		OrderDto 주문 = 주문_등록되어_있음(주문(비어있지않은_주문_테이블.getId(), 후라이드후라이드_메뉴.getId(), 1)).as(OrderDto.class);
		주문_상태_변경되어_있음(주문.getId(), OrderStatus.MEAL);

		// when
		ExtractableResponse<Response> response = 주문_테이블_빈_상태_변경_요청(비어있지않은_주문_테이블.getId(), true);

		// then
		주문_테이블_빈_상태_변경되지_않음(response);
	}

	@DisplayName("주문 테이블에 손님 수를 변경할 수 있다")
	@Test
	void changeNumberOfGuests() {
		// given
		OrderTableDto 비어있지않은_주문_테이블 = 주문_테이블_등록되어_있음(비어있지않은_주문_테이블()).as(OrderTableDto.class);
		int numberOfGuests = 6;

		// when
		ExtractableResponse<Response> response = 주문_테이블_손님_수_변경_요청(비어있지않은_주문_테이블.getId(), numberOfGuests);

		// then
		주문_테이블_손님_수_변경됨(response);
		주문_테이블_손님_수_일치함(response, numberOfGuests);
	}

	@DisplayName("손님 수가 0보다 작은 경우 주문 테이블에 손님 수를 변경할 수 없다")
	@Test
	void changeNumberOfGuestsFailOnNegative() {
		// given
		OrderTableDto 비어있지않은_주문_테이블 = 주문_테이블_등록되어_있음(비어있지않은_주문_테이블()).as(OrderTableDto.class);
		int numberOfGuests = -1;

		// when
		ExtractableResponse<Response> response = 주문_테이블_손님_수_변경_요청(비어있지않은_주문_테이블.getId(), numberOfGuests);

		// then
		주문_테이블_손님_수_변경되지_않음(response);
	}

	@DisplayName("빈 주문 테이블인 경우 주문 테이블에 손님 수를 변경할 수 없다")
	@Test
	void changeNumberOfGuestsFailOnEmpty() {
		// given
		OrderTableDto 빈_주문_테이블 = 주문_테이블_등록되어_있음(빈_주문_테이블()).as(OrderTableDto.class);
		int numberOfGuests = 6;

		// when
		ExtractableResponse<Response> response = 주문_테이블_손님_수_변경_요청(빈_주문_테이블.getId(), numberOfGuests);

		// then
		주문_테이블_손님_수_변경되지_않음(response);
	}

	private void 주문_테이블_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotBlank();
		assertThat(response.as(OrderTableDto.class).getId()).isNotNull();
	}

	private ExtractableResponse<Response> 주문_테이블_목록_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get("/api/tables")
			.then().log().all()
			.extract();
	}

	private void 주문_테이블_목록_응답됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 주문_테이블_목록_포함됨(ExtractableResponse<Response> response, List<OrderTableDto> expectedOrderTables) {
		List<Long> actualIds = response.jsonPath().getList(".", OrderTableDto.class).stream()
			.map(OrderTableDto::getId)
			.collect(Collectors.toList());

		List<Long> expectedIds = expectedOrderTables.stream()
			.map(OrderTableDto::getId)
			.collect(Collectors.toList());

		assertThat(actualIds).isEqualTo(expectedIds);
	}

	private ExtractableResponse<Response> 주문_테이블_빈_상태_변경_요청(Long orderTableId, boolean empty) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTableRequest(empty))
			.when().put("/api/tables/{orderTableId}/empty", orderTableId)
			.then().log().all()
			.extract();
	}

	private void 주문_테이블_빈_상태_변경됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 주문_테이블_빈_상태_변경되지_않음(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	private void 주문_테이블_빈_상태_일치함(ExtractableResponse<Response> response, boolean empty) {
		OrderTableDto actual = response.as(OrderTableDto.class);
		assertThat(actual.isEmpty()).isEqualTo(empty);
	}

	private ExtractableResponse<Response> 주문_테이블_손님_수_변경_요청(Long orderTableId, int numberOfGuests) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(new OrderTableRequest(numberOfGuests))
			.when().put("/api/tables/{orderTableId}/number-of-guests", orderTableId)
			.then().log().all()
			.extract();
	}

	private void 주문_테이블_손님_수_변경됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	private void 주문_테이블_손님_수_일치함(ExtractableResponse<Response> response, int expectedNumberOfGuests) {
		OrderTableDto actual = response.as(OrderTableDto.class);
		assertThat(actual.getNumberOfGuests()).isEqualTo(expectedNumberOfGuests);
	}

	private void 주문_테이블_손님_수_변경되지_않음(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
