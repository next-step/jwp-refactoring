package kitchenpos.acceptance.table;

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

public class TableRestAcceptance extends AcceptanceTest {

	public static final String TABLE_REQUEST_URL = "/api/tables";

	public static ExtractableResponse<Response> 주문_테이블_등록_요청(OrderTable orderTable) {
		return RestAssured
			.given().log().all()
			.body(orderTable)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post(TABLE_REQUEST_URL)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 주문_테이블_조회_요청() {
		ExtractableResponse<Response> response = RestAssured
			.given().log().all()
			.when().get(TABLE_REQUEST_URL)
			.then().log().all().extract();
		return response;
	}

	public static ExtractableResponse<Response> 주문_빈_테이블_등록되어있음() {
		return 주문_테이블_등록_요청(OrderTable.of(null, null, 0, true));
	}

	public static ExtractableResponse<Response> 주문_테이블_상태변경_요청(OrderTable orderTable, OrderTable expected) {
		return RestAssured
			.given().log().all()
			.body(expected)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().put(TABLE_REQUEST_URL + "/{orderTableId}/empty", expected.getId())
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 주문_테이블_인원변경_요청(Long orderTableId, OrderTable expected) {
		return RestAssured
			.given().log().all()
			.body(expected)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().put(TABLE_REQUEST_URL + "/{orderTableId}/number-of-guests", orderTableId)
			.then().log().all().extract();
	}

	public static void 주문_테이블_등록됨(ExtractableResponse<Response> response) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(response.as(OrderTable.class)).isNotNull(),
			() -> assertThat(response.as(OrderTable.class).getId()).isNotNull()
		);
	}

	public static void 주문_테이블_목록_포함됨(ExtractableResponse<Response> response,
		List<ExtractableResponse<Response>> expected) {
		List<Long> expectedOrderTableIds = expected.stream()
			.map(it -> it.as(OrderTable.class).getId())
			.collect(Collectors.toList());

		List<Long> resultOrderTableIds = response.jsonPath().getList(".", OrderTable.class).stream()
			.map(OrderTable::getId)
			.collect(Collectors.toList());

		assertThat(resultOrderTableIds).containsAll(expectedOrderTableIds);
	}

	public static void 주문_테이블_목록_조회됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
	}

	public static void 주문_테이블_인원변경됨(ExtractableResponse<Response> response, OrderTable actual) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.as(OrderTable.class).getNumberOfGuests()).isEqualTo(actual.getNumberOfGuests())
		);
	}

	public static void 주문_테이블_상태변경됨(ExtractableResponse<Response> response, boolean result) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value()),
			() -> assertThat(response.as(OrderTable.class).isEmpty()).isEqualTo(result)
		);
	}
}
