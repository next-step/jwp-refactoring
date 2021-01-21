package kitchenpos.acceptance.table;

import java.util.Arrays;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;

@DisplayName("주문 테이블 테스트")
public class TableRestAcceptanceTest extends TableRestAcceptance {

	@DisplayName("주문 테이블을 생성한다.")
	@Test
	void createOrderTableTest() {
		// given
		OrderTable orderTable = OrderTable.of(null, null, 0, true);

		// when
		ExtractableResponse<Response> response = 주문_테이블_등록_요청(orderTable);

		// then
		주문_테이블_등록됨(response);
	}

	@DisplayName("주문 테이블 목록을 조회한다.")
	@Test
	void selectOrderTablesTest() {
		// given
		ExtractableResponse<Response> createResponse1 = 주문_테이블_등록_요청(OrderTable.of(null, null, 0, false));
		ExtractableResponse<Response> createResponse2 = 주문_테이블_등록_요청(OrderTable.of(null, null, 0, false));
		ExtractableResponse<Response> createResponse3 = 주문_테이블_등록_요청(OrderTable.of(null, 1L, 5, true));

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
		OrderTable expected = 주문_테이블_등록_요청(OrderTable.of(null, null, 0, true)).as(OrderTable.class);

		// when
		ExtractableResponse<Response> response = 주문_테이블_상태변경_요청(expected, OrderTable.of(null, null, 2, false));

		// then
		주문_테이블_상태변경됨(response, false);
	}

	@DisplayName("주문 테이블 인원수를 변경한다.")
	@Test
	void changeNumberOfGuestsTest() {
		// given
		OrderTable expected = 주문_테이블_등록_요청(OrderTable.of(null, null, 5, false)).as(OrderTable.class);
		OrderTable actual = OrderTable.of(null, null, 10, false);
		// when
		ExtractableResponse<Response> response = 주문_테이블_인원변경_요청(expected.getId(), actual);

		// then
		주문_테이블_인원변경됨(response, actual);
	}
}
