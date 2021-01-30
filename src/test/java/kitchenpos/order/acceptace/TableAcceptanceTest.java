package kitchenpos.order.acceptace;

import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;

public class TableAcceptanceTest extends AcceptanceTest {
	@DisplayName("테이블 생성")
	@Test
	void create() {
		ExtractableResponse<Response> createResponse = 테이블_생성_요청(0, true);

		테이블_생성_성공(createResponse);
	}

	@DisplayName("테이블 조회")
	@Test
	void list() {
		테이블_생성됨(0, true);
		테이블_생성됨(0, true);

		ExtractableResponse<Response> response = 테이블_조회_요청();

		테이블_조회_성공(response);
	}

	@DisplayName("빈 테이블 변경")
	@Test
	void changeEmpty() {
		boolean empty = false;
		테이블_생성됨(0, true);

		ExtractableResponse<Response> response = 빈_테이블_변경_요청(1L, empty);

		빈_테이블_변경_성공(response, empty);
	}

	@DisplayName("단체 테이블 변경시 실패")
	@Test
	void changeEmptyWhenGroupTable() {
		테이블_생성됨(0, true);
		테이블_생성됨(0, true);
		TableGroupAcceptanceTest.단체_지정됨(new OrderTableRequest(1L), new OrderTableRequest(2L));

		ExtractableResponse<Response> response = 빈_테이블_변경_요청(1L, false);

		빈_테이블_변경_실패(response);
	}

	@DisplayName("손님 수 변경")
	@Test
	void changeGuestsNumber() {
		OrderTableResponse orderTableResponse = 테이블_생성_요청(0, false).as(OrderTableResponse.class);

		ExtractableResponse<Response> response = 테이블_손님_수_변경_요청(orderTableResponse.getId(), 2);

		테이블_손님_수_변경_성공(response, 2);
	}

	@DisplayName("빈 테이블 손님수 변경시 변경시 실패")
	@Test
	void changeGuestsNumberWhenEmptyTable() {
		테이블_생성됨(0, true);

		ExtractableResponse<Response> response = 테이블_손님_수_변경_요청(1L, 2);

		테이블_손님_수_변경_실패(response, 2);
	}

	public static void 테이블_생성됨(int numberOfGuests, boolean empty) {
		테이블_생성_성공(테이블_생성_요청(numberOfGuests, empty));
	}

	public static ExtractableResponse<Response> 테이블_생성_요청(int numberOfGuests, boolean empty) {
		OrderTableRequest orderTableRequest = new OrderTableRequest(numberOfGuests, empty);

		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderTableRequest)
			.when().post("/api/tables")
			.then().log().all()
			.extract();
	}

	public static void 테이블_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	public static ExtractableResponse<Response> 테이블_조회_요청() {
		return RestAssured.given().log().all()
			.when().get("/api/tables")
			.then().log().all()
			.extract();
	}

	public static void 테이블_조회_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		List<OrderTableResponse> orderTableResponses = response.jsonPath().getList(".", OrderTableResponse.class);
		assertThat(orderTableResponses)
			.map(OrderTableResponse::getId)
			.allMatch(Objects::nonNull);
	}

	public static ExtractableResponse<Response> 빈_테이블_변경_요청(Long id, boolean empty) {
		OrderTableRequest orderTableRequest = new OrderTableRequest(empty);

		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderTableRequest)
			.when().put("/api/tables/{orderTableId}/empty", id)
			.then().log().all()
			.extract();
	}

	public static void 빈_테이블_변경_성공(ExtractableResponse<Response> response, boolean empty) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
		assertThat(orderTableResponse.isEmpty()).isEqualTo(empty);
	}

	public static void 빈_테이블_변경_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public static ExtractableResponse<Response> 테이블_손님_수_변경_요청(Long id, int number) {
		OrderTableRequest orderTableRequest = new OrderTableRequest(number);

		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderTableRequest)
			.when().put("/api/tables/{orderTableId}/number-of-guests", id)
			.then().log().all()
			.extract();
	}

	public static void 테이블_손님_수_변경_성공(ExtractableResponse<Response> response, int number) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		OrderTableResponse orderTableResponse = response.as(OrderTableResponse.class);
		assertThat(orderTableResponse.getNumberOfGuests()).isEqualTo(number);
	}

	public static void 테이블_손님_수_변경_실패(ExtractableResponse<Response> response, int number) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}
}
