package kitchenpos.acceptance.ordertable;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;

@DisplayName("주문 테이블 테스트")
public class TableRestAcceptanceTest extends TableRestAcceptance {

	@DisplayName("주문 테이블을 생성한다.")
	@Test
	void createOrderTableTest() {
		// given
		OrderTableRequest request = OrderTableRequest.of(0, true);

		// when
		ExtractableResponse<Response> response = 주문_테이블_등록_요청(request);

		// then
		주문_테이블_등록됨(response);
	}

	@DisplayName("주문 테이블 목록을 조회한다.")
	@Test
	void selectOrderTablesTest() {
		// given
		ExtractableResponse<Response> createResponse1 = 주문_테이블_등록되어있음(0, true);
		ExtractableResponse<Response> createResponse2 = 주문_테이블_등록되어있음(0, true);
		ExtractableResponse<Response> createResponse3 = 주문_테이블_등록되어있음(5, false);

		// when
		ExtractableResponse<Response> response = 주문_테이블_조회_요청();

		// then
		주문_테이블_목록_조회됨(response);
		주문_테이블_목록_포함됨(response, Arrays.asList(createResponse1, createResponse2, createResponse3));
	}

	@DisplayName("주문 테이블상태 변경을 한다.")
	@Test
	void changeEmptyTableTest() {
		// given
		OrderTableRequest request = OrderTableRequest.of(0, false);
		OrderTableResponse expected = 주문_테이블_등록되어있음(0, true).as(OrderTableResponse.class);

		// when
		ExtractableResponse<Response> response = 주문_테이블_상태변경_요청(expected.getId(), request);

		// then
		주문_테이블_상태변경됨(response, request.isEmpty());
	}

	@DisplayName("주문 테이블 인원수를 변경한다.")
	@Test
	void changeNumberOfGuestsTest() {
		// given
		OrderTableRequest request = OrderTableRequest.of(10, false);
		OrderTableResponse expected = 주문_테이블_등록되어있음(5, false).as(OrderTableResponse.class);

		// when
		ExtractableResponse<Response> response = 주문_테이블_인원변경_요청(expected.getId(), request);

		// then
		주문_테이블_인원변경됨(response, request);
	}
}
