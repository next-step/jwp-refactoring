package kitchenpos.acceptance;

import static kitchenpos.acceptance.utils.TableAcceptanceUtils.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.ui.response.OrderTableResponse;

@DisplayName("주문 테이블 관련 기능")
class TableAcceptanceTest extends AcceptanceTest {

	/**
	 * given 주문 테이블이 등록되어 있고
	 * when 주문 테이블 목록을 조회하면
	 * then 주문 테이블 목록이 조회된다
	 */
	@DisplayName("주문 테이블을 등록할 수 있다.")
	@Test
	void createTableTest() {
		// given
		int numberOfGuests = 2;
		boolean empty = false;

		// when
		ExtractableResponse<Response> 주문_테이블_생성_요청 = 주문_테이블_생성_요청(numberOfGuests, empty);

		// then
		주문_테이블_생성됨(주문_테이블_생성_요청, numberOfGuests, empty);
	}

	/**
	 * given 주문 테이블이 등록되어 있고
	 * when 주문 테이블 목록을 조회하면
	 * then 주문 테이블 목록이 조회된다
	 */
	@DisplayName("주문 테이블들을 조회할 수 있다.")
	@Test
	void listTableTest() {
		// given
		int numberOfGuests = 2;
		boolean empty = false;
		주문_테이블_등록_되어_있음(numberOfGuests, empty);

		// when
		ExtractableResponse<Response> 주문_테이블_목록_조회_요청 = 주문_테이블_목록_조회_요청();

		// then
		주문_테이블_목록_조회됨(주문_테이블_목록_조회_요청, numberOfGuests, empty);
	}

	/**
	 * given 주문 테이블이 등록되어 있고
	 * when 주문 테이블의 빈 테이블 상태를 변경하면
	 * then 주문 테이블의 상태가 비어있는 상태가 된다.
	 */
	@DisplayName("주문 테이블의 빈 테이블 상태를 변경할 수 있다.")
	@Test
	void changeEmptyTest() {
		// given
		boolean empty = true;
		OrderTableResponse orderTable = 주문_테이블_등록_되어_있음(2, false);

		// when
		ExtractableResponse<Response> 빈_테이블로_수정_요청 = 빈_테이블로_수정_요청(orderTable.getId(), true);

		// then
		빈_테이블로_수정됨(빈_테이블로_수정_요청, empty);
	}

	/**
	 * given 주문 테이블이 등록되어 있고
	 * when 주문 테이블의 손님 수를 변경하면
	 * then 주문 테이블의 손님 수가 변경된다.
	 */
	@DisplayName("주문 테이블의 손님 수를 변경할 수 있다.")
	@Test
	void changeNumberOfGuestsTest() {
		// given
		int expectedNumber = 2;
		OrderTableResponse orderTable = 주문_테이블_등록_되어_있음(5, false);

		// when
		ExtractableResponse<Response> 손님_수_수정_요청 = 손님_수_수정_요청(orderTable.getId(), expectedNumber);

		// then
		손님_수_수정됨(손님_수_수정_요청, expectedNumber);
	}
}
