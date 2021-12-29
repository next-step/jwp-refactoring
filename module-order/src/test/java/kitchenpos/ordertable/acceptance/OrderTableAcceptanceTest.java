package kitchenpos.ordertable.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;

class OrderTableAcceptanceTest extends AcceptanceTest {

	private static OrderTableRequest changeEmptyRequest = new OrderTableRequest(true);
	private static OrderTableRequest changeGuestsRequest = new OrderTableRequest(4);

	@Test
	@DisplayName("테이블 생성 테스트")
	public void createTableTest() {
		//given
		OrderTableRequest orderTableRequest = new OrderTableRequest(0, true);

		//when
		ExtractableResponse<Response> response = 테이블_생성_요청(orderTableRequest);

		//then
		테이블_생성_성공(response);
	}

	@Test
	@DisplayName("테이블 목록 조회 테스트")
	public void findAllTableTest() {
		//given
		//when
		ExtractableResponse<Response> response = 테이블_목록_조회_요청();

		//then
		테이블_목록_조회_성공(response);
	}

	@Test
	@DisplayName("테이블 빈 상태로 변경 테스트")
	public void changeEmptyTest() {
		//given
		OrderTableRequest emptyRequest = new OrderTableRequest(true);

		//when
		ExtractableResponse<Response> response = 테이블_상태_변경_요청(emptyRequest, 3L);

		//then
		테이블_상태_변경_성공(emptyRequest, response);
	}

	@Test
	@DisplayName("테이블이 존재하지 않아서 변경 실패")
	public void changeEmptyFailNotExistedOrderTable() {
		//given
		//when
		ExtractableResponse<Response> response = 테이블_상태_변경_요청(changeEmptyRequest, 99L);

		//then
		테이블관련_요청_실패(response, "테이블이 존재하지 않습니다");
	}

	@Test
	@DisplayName("테이블이 이미 그릅화되서 변경 실패")
	public void changeEmptyFailAlreadyGroupOrderTable() {
		//given
		//when
		ExtractableResponse<Response> response = 테이블_상태_변경_요청(changeEmptyRequest, 8L);

		//then
		테이블관련_요청_실패(response, "그룹화 된 테이블은 상태를 변경 할 수 없습니다");
	}

	@Test
	@DisplayName("테이블이 이미 그릅화되서 변경 실패")
	public void changeEmptyFailOrderTableIsNotCompletion() {
		//given
		//when
		ExtractableResponse<Response> response = 테이블_상태_변경_요청(changeEmptyRequest, 4L);

		//then
		테이블관련_요청_실패(response, "테이블의 주문이 계산완료 되지 않았습니다");
	}

	@Test
	@DisplayName("테이블 손님수 변경 테스트")
	public void changeNumberOfGuests() {
		//given
		//when
		ExtractableResponse<Response> response = 테이블_손님수_변경_요청(changeGuestsRequest, 4L);

		//then
		테이블_손님수_변경_성공(response);
	}

	@Test
	@DisplayName("테이블 손님수 0보다 작게 요청해서 변경 실패")
	public void changeNumberOfGuestsFailLessThanZero() {
		//given
		OrderTableRequest changeGuestsRequest = new OrderTableRequest(-1);

		//when
		ExtractableResponse<Response> response = 테이블_손님수_변경_요청(changeGuestsRequest, 1L);

		//then
		테이블관련_요청_실패(response, "인원수는 0보다 작을 수 없습니다");
	}

	@Test
	@DisplayName("손님 수를 변경할 테이블이 미 존재")
	public void changeNumberOfGuestsFailTableIsNotExisted() {
		//given
		//when
		ExtractableResponse<Response> response = 테이블_손님수_변경_요청(changeGuestsRequest, 99L);

		//then
		테이블관련_요청_실패(response, "테이블이 존재하지 않습니다");
	}

	@Test
	@DisplayName("손님 수를 변경할 테이블이 미 사용상태(비어있음)")
	public void changeNumberOfGuestsFailTableIsNotUse() {
		//given
		//when
		ExtractableResponse<Response> response = 테이블_손님수_변경_요청(changeGuestsRequest, 1L);

		//then
		테이블관련_요청_실패(response, "비어 있는 테이블입니다");
	}

	private void 테이블_손님수_변경_성공(ExtractableResponse<Response> response) {
		OrderTableResponse tableResponse = response.as(OrderTableResponse.class);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(tableResponse.getNumberOfGuests()).isEqualTo(changeGuestsRequest.getNumberOfGuests());
	}

	private ExtractableResponse<Response> 테이블_손님수_변경_요청(OrderTableRequest changeNumberOfGuestsRequest, long id) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(changeNumberOfGuestsRequest)
			.when().put("/api/tables/{orderTableId}/number-of-guests", id)
			.then().log().all()
			.extract();
	}

	private void 테이블_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isEqualTo("/api/tables/10");
	}

	private ExtractableResponse<Response> 테이블_생성_요청(OrderTableRequest orderTableRequest) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(orderTableRequest)
			.when().post("/api/tables/")
			.then().log().all()
			.extract();
	}

	private void 테이블_목록_조회_성공(ExtractableResponse<Response> response) {
		List<OrderTableResponse> orderTableResponses = response.jsonPath().getList(".", OrderTableResponse.class);
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(orderTableResponses).hasSizeGreaterThanOrEqualTo(9);
	}

	private ExtractableResponse<Response> 테이블_목록_조회_요청() {
		return RestAssured.given().log().all()
			.accept(MediaType.APPLICATION_JSON_VALUE)
			.when().get("/api/tables")
			.then().log().all()
			.extract();
	}

	private void 테이블관련_요청_실패(ExtractableResponse<Response> response, String message) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.body().asString()).isEqualTo(message);
	}

	private void 테이블_상태_변경_성공(OrderTableRequest emptyRequest, ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.as(OrderTableResponse.class).isEmpty()).isEqualTo(emptyRequest.isEmpty());
	}

	private ExtractableResponse<Response> 테이블_상태_변경_요청(OrderTableRequest emptyRequest, Long id) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(emptyRequest)
			.when().put("/api/tables/{orderTableId}/empty", id)
			.then().log().all()
			.extract();
	}
}
