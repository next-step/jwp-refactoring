package kitchenpos.acceptance.ordertable;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.util.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;

public class TableRestAcceptance extends AcceptanceTest {

	public static final String TABLE_REQUEST_URL = "/api/tables";

	public static ExtractableResponse<Response> 주문_테이블_등록_요청(OrderTableRequest request) {
		return RestAssured
			.given().log().all()
			.body(request)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post(TABLE_REQUEST_URL)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 주문_테이블_등록되어있음(final int numberOfGuests, final boolean empty) {
		return 주문_테이블_등록_요청(OrderTableRequest.of(numberOfGuests, empty));
	}

	public static ExtractableResponse<Response> 주문_빈_테이블_등록되어있음() {
		return 주문_테이블_등록되어있음(0, true);
	}

	public static ExtractableResponse<Response> 주문_테이블_조회_요청() {
		return RestAssured
			.given().log().all()
			.when().get(TABLE_REQUEST_URL)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 주문_테이블_상태변경_요청(Long id, OrderTableRequest expected) {
		return RestAssured
			.given().log().all()
			.body(expected)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().put(TABLE_REQUEST_URL + "/{orderTableId}/empty", id)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 주문_테이블_인원변경_요청(Long orderTableId, OrderTableRequest request) {
		return RestAssured
			.given().log().all()
			.body(request)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().put(TABLE_REQUEST_URL + "/{orderTableId}/number-of-guests", orderTableId)
			.then().log().all().extract();
	}

	public static void 주문_테이블_등록됨(ExtractableResponse<Response> response) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(response.as(OrderTableResponse.class)).isNotNull(),
			() -> assertThat(response.as(OrderTableResponse.class).getId()).isNotNull()
		);
	}

	public static void 주문_테이블_목록_포함됨(ExtractableResponse<Response> response,
		List<ExtractableResponse<Response>> expected) {
		List<Long> expectedOrderTableIds = expected.stream()
			.map(it -> it.as(OrderTableResponse.class).getId())
			.collect(Collectors.toList());

		List<Long> resultOrderTableIds = response.jsonPath().getList(".", OrderTableResponse.class).stream()
			.map(OrderTableResponse::getId)
			.collect(Collectors.toList());

		assertThat(resultOrderTableIds).containsAll(expectedOrderTableIds);
	}

	public static void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 주문_테이블_인원변경됨(ExtractableResponse<Response> response, OrderTableRequest actual) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.as(OrderTable.class).getNumberOfGuests()).isEqualTo(
				actual.getNumberOfGuests())
		);
	}

	public static void 주문_테이블_상태변경됨(ExtractableResponse<Response> response, boolean result) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.as(OrderTable.class).isEmpty()).isEqualTo(result)
		);
	}
}
