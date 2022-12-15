package kitchenpos.acceptance.ordertable;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest2;
import kitchenpos.acceptance.tablegroup.TableGroupAcceptanceTestStep;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.OrderTableResponse;

@DisplayName("주문 테이블 관리")
class OrderTableAcceptanceTest extends AcceptanceTest2 {

	OrderTableResponse 주문_테이블;

	OrderTableAcceptanceTestStep step = new OrderTableAcceptanceTestStep();
	TableGroupAcceptanceTestStep tableGroup = new TableGroupAcceptanceTestStep();

	/**
	 * Feature: 주문 테이블 관리 기능
	 * Background
	 *   When 주문 테이블 등록을 요청하면
	 *   Then 주문 테이블 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		OrderTableRequest 주문_테이블 = OrderTableFixture.주문_테이블();
		ExtractableResponse<Response> 등록_요청_응답 = step.등록_요청(주문_테이블);
		this.주문_테이블 = step.등록됨(등록_요청_응답);
	}

	/**
	 * Scenario: 주문 테이블 관리
	 * When 주문 테이블을 주문 불가 테이블로 수정을 요청하면
	 * Then 주문 테이블 수정에 성공한다
	 * When 주문 테이블의 손님 수 수정을 요청하면
	 * Then 주문 테이블 손님 수 수정에 성공한다
	 */
	@Test
	void 주문_테이블_관리() {
		// when
		OrderTableRequest 주문_불가_테이블 = OrderTableFixture.주문_불가_테이블(주문_테이블);
		ExtractableResponse<Response> 수정_요청_응답 = step.빈_주문_테이블_수정_요청(주문_테이블.getId(), 주문_불가_테이블);
		// then
		테이블_수정됨(수정_요청_응답);

		// when
		OrderTableRequest 수정된_주문_테이블 = OrderTableFixture.주문_테이블(1);
		수정_요청_응답 = step.손님_수_수정_요청(주문_테이블.getId(), 수정된_주문_테이블);
		// then
		테이블_수정됨(수정_요청_응답);
	}

	/**
	 * When 주문 테이블이 존재하지 않을 경우
	 * Then 주문 테이블 수정에 실패한다
	 */
	@Test
	void 주문_테이블이_존재하지_않음() {
		// when
		OrderTableRequest 주문_테이블 = OrderTableFixture.주문_테이블();
		ExtractableResponse<Response> 수정_응답 = step.손님_수_수정_요청(-1L, 주문_테이블);
		// then
		step.수정_실패함(수정_응답);
	}

	/**
	 * When 방문한 손님 수가 0명 미만일 경우
	 * Then 주문 테이블 수정에 실패한다
	 */
	@Test
	void 방문_손님_수가_0명_미만() {
		// when
		OrderTableRequest 수정된_주문_테이블 = OrderTableFixture.주문_테이블(-1);
		ExtractableResponse<Response> 수정_응답 = step.손님_수_수정_요청(this.주문_테이블.getId(), 수정된_주문_테이블);

		// when
		step.수정_실패함(수정_응답);
	}

	/**
	 * Given 빈 테이블이 아닌 테이블을 등록하고
	 * When 빈 테이블이 아닌 테이블의 손님 수 변경을 요청하면
	 * Then 주문 테이블 수정에 실패한다
	 */
	@Test
	void 빈_테이블이_아님() {
		// given
		OrderTableRequest 주문불가_테이블 = OrderTableFixture.주문_테이블(1, false);
		ExtractableResponse<Response> 등록_응답 = step.등록_요청(주문불가_테이블);
		OrderTableResponse 등록된_테이블 = step.등록됨(등록_응답);

		OrderTableRequest 수정된_주문_테이블 = OrderTableFixture.주문_테이블(3, false);
		ExtractableResponse<Response> 수정_응답 = step.손님_수_수정_요청(등록된_테이블.getId(), 수정된_주문_테이블);

		step.수정_실패함(수정_응답);
	}

	/**
	 * Given 주문 테이블이 2개 이상 등록되어 있고
	 * Given 그룹 테이블에 해당 주문 테이블들을 생성하고
	 * When 주문 테이블을 빈 테이블로 수정 요청할 경우
	 * Then 주문 테이블 수정에 실패한다
	 */
	// @Test
	// void 주문_테이블이_그룹_테이블에_존재함() {
	// 	// given
	// 	List<OrderTable> 주문_테이블_목록 = 테이블_등록됨(주문_테이블(), 주문_테이블());
	//
	// 	// given
	// 	TableGroup 테이블_그룹 = 그룹_테이블_등록되어_있음(주문_테이블_목록);
	//
	// 	// when
	// 	OrderTable 수정_주문_테이블 = 테이블_그룹.getOrderTables().get(0);
	// 	수정_주문_테이블.setEmpty(false);
	// 	ExtractableResponse<Response> 수정_응답 = step.빈_주문_테이블_수정_요청(수정_주문_테이블);
	//
	// 	// then
	// 	step.수정_실패함(수정_응답);
	// }

	/**
	 * Given 주문 테이블이 등록되어 있고
	 * Given 주문 상태가 '조리중' 또는 '식사중'인 주문이 등록되어 있을 때
	 * When 주문 테이블을 빈 테이블로 변경 요청할 경우
	 * Then 주문 테이블 수정에 실패한다
	 */
	// @Test
	// void 주문_상태가_조리중_이거나_식사중임() {
	// 	// given
	// 	OrderTable 주문_테이블 = 주문_테이블_등록되어_있음(주문_테이블(10));
	// 	// given
	// 	orders.주문_등록되어_있음(주문_테이블);
	// 	// when
	// 	ExtractableResponse<Response> 수정_요청_응답 = step.빈_주문_테이블_수정_요청(주문_테이블);
	// 	// then
	// 	step.수정_실패함(수정_요청_응답);
	// }

	private void 테이블_수정됨(ExtractableResponse<Response> 수정_요청_응답) {
		step.수정됨(수정_요청_응답);
		step.수정됨(수정_요청_응답);
	}
}
