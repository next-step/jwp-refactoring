package kitchenpos.ordertable.ui;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

class OrderTableAcceptanceTest extends AcceptanceTest {

	@DisplayName("주문 테이블 목록을 조회한다.")
	@Test
	void list() {
		//when
		ExtractableResponse<Response> response = RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .when().get("/api/tables")
			  .then().log().all()
			  .extract();

		//then
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(response.jsonPath().getList(".", OrderTableResponse.class)).isNotEmpty();
	}

	@DisplayName("주문 테이블을 관리한다.")
	@Test
	void manage() {
		//테이블 정보를 등록한다.
		//given
		OrderTableRequest request = new OrderTableRequest();
		//when
		ExtractableResponse<Response> response = 주문_테이블_등록을_요청한다(request);
		//then
		주문_테이블이_등록됨(response);

		//테이블 상태를 변경한다.
		//given
		OrderTableResponse createResponseDto = response.body().as(OrderTableResponse.class);
		OrderTableRequest changeEmptyRequest = new OrderTableRequest(createResponseDto.getId(),
			  createResponseDto.getTableGroupId(),
			  createResponseDto.getNumberOfGuests(), !createResponseDto.isEmpty());
		//when
		ExtractableResponse<Response> changeEmptyResponse = 테이블_상태를_변경_요청한다(
			  createResponseDto.getId(), changeEmptyRequest);
		//then
		테이블_상태가_변경됨(changeEmptyResponse);

		//테이블 인원수를 변경한다.
		//given
		Long orderTableId = changeEmptyResponse.body().as(OrderTableResponse.class).getId();
		OrderTableRequest changeNumberOfGuestRequest = new OrderTableRequest(
			  createResponseDto.getId(),
			  createResponseDto.getTableGroupId(),
			  5, !createResponseDto.isEmpty());
		//when
		ExtractableResponse<Response> changeNumberOfGuest = 테이블_인원수를_변경_요청한다(orderTableId,
			  changeNumberOfGuestRequest);
		//then
		테이블_인원수가_변경됨(changeEmptyResponse, changeNumberOfGuestRequest, changeNumberOfGuest);
	}

	private ExtractableResponse<Response> 주문_테이블_등록을_요청한다(
		  OrderTableRequest request) {
		return RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(request)
			  .when().post("/api/tables")
			  .then().log().all()
			  .extract();
	}

	private ExtractableResponse<Response> 테이블_상태를_변경_요청한다(
		  Long orderTableId, OrderTableRequest request) {
		return RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(request)
			  .when().put("/api/tables/" + orderTableId + "/empty")
			  .then().log().all()
			  .extract();
	}

	private ExtractableResponse<Response> 테이블_인원수를_변경_요청한다(Long orderTableId,
		  OrderTableRequest request) {
		return RestAssured.given().log().all()
			  .contentType(MediaType.APPLICATION_JSON_VALUE)
			  .body(request)
			  .when().put("/api/tables/" + orderTableId + "/number-of-guests")
			  .then().log().all()
			  .extract();
	}

	private void 주문_테이블이_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isNotEmpty();
	}

	private void 테이블_상태가_변경됨(ExtractableResponse<Response> changeEmptyResponse) {
		assertThat(changeEmptyResponse.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeEmptyResponse.as(OrderTableResponse.class)).isNotNull();
	}

	private void 테이블_인원수가_변경됨(ExtractableResponse<Response> response,
		  OrderTableRequest request,
		  ExtractableResponse<Response> changeNumberOfGuest) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.OK.value());
		assertThat(changeNumberOfGuest.body().as(OrderTableResponse.class).getNumberOfGuests())
			  .isEqualTo(request.getNumberOfGuests());
	}
}
