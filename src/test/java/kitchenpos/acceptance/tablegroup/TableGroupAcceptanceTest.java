package kitchenpos.acceptance.tablegroup;

import static org.assertj.core.util.Lists.newArrayList;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest2;
import kitchenpos.acceptance.ordertable.OrderTableAcceptanceTestStep;
import kitchenpos.acceptance.ordertable.OrderTableFixture;
import kitchenpos.ui.dto.OrderTableRequest;
import kitchenpos.ui.dto.OrderTableResponse;
import kitchenpos.ui.dto.TableGroupRequest;
import kitchenpos.ui.dto.TableGroupResponse;

@DisplayName("테이블 그룹 관리")
class TableGroupAcceptanceTest extends AcceptanceTest2 {


	List<OrderTableResponse> 주문_테이블_목록;
	TableGroupAcceptanceTestStep step = new TableGroupAcceptanceTestStep();
	OrderTableAcceptanceTestStep orderTables = new OrderTableAcceptanceTestStep();
	OrderTableAcceptanceTestStep orders = new OrderTableAcceptanceTestStep();

	/**
	 * Feature: 테이블 그룹 관리 기능
	 * Background
	 *   when 주문 테이블 등록을 요청하면
	 *   then 주문 테이블 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		주문_테이블_목록 = 주문_테이블_등록되어_있음(OrderTableFixture.주문_테이블(), OrderTableFixture.주문_테이블());
	}

	/**
	 * Scenario: 테이블 그룹을 관리한다
	 * When 테이블 그룹 등록을 요청하면
	 * Then 테이블 그룹 등록에 성공한다
	 * When 테이블 그룹 해제를 요청하면
	 * Then 테이블 그룹 해제에 성공한다
	 */
	@Test
	void 테이블_그룹_생성() {
		// when
		TableGroupRequest 주문_테이블_그룹 = TableGroupFixture.주문_테이블_그룹(주문_테이블_목록);
		ExtractableResponse<Response> 등록_응답 = step.등록_요청(주문_테이블_그룹);
		// then
		TableGroupResponse 등록된_주문_테이블 = step.등록됨(등록_응답);

		// when
		ExtractableResponse<Response> 해제_요청_응답 = 테이블_그룹_해제_요청(등록된_주문_테이블);
		테이블_그룹_해제됨(해제_요청_응답);
	}

	/**
	 * Given 주문 테이블이 생성되어 있지 않을경우
	 * When 테이블 그룹 생성을 요청하면
	 * Then 테이블 그룹 생성에 실패한다
	 */
	@Test
	void 주문_테이블이_존재하지_않음() {
		OrderTableResponse 새_주문_테이블 = OrderTableFixture.주문_테이블(1L);
		TableGroupRequest 주문_테이블_그룹 = TableGroupFixture.주문_테이블_그룹(newArrayList(새_주문_테이블));

		ExtractableResponse<Response> 등록_요청_응답 = step.등록_요청(주문_테이블_그룹);

		step.등록_실패함(등록_요청_응답);
	}

	/**
	 * Given 주문 테이블 목록이 2개 미만일 경우
	 * When 테이블 그룹 생성을 요청하면
	 * Then 테이블 그룹 생성에 실패한다
	 */
	@Test
	void 주문_테이블_목록이_2개_미만() {
		TableGroupRequest 주문_테이블_그룹 = TableGroupFixture.주문_테이블_그룹(주문_테이블_목록);

		ExtractableResponse<Response> 등록_요청_응답 = step.등록_요청(주문_테이블_그룹);

		step.등록_실패함(등록_요청_응답);
	}

	/**
	 * Given 두 개의 주문 테이블이 등록되어 있고
	 * Given 주문 테이블 그룹이 등록되어 있고
	 * Given 주문이 등록되어 있고
	 * When 테이블 그룹 해제를 요청하면
	 * Then 테이블 그룹 해제가 실패한다.
	 */
	// @Test
	// void 주문_테이블_그룹_해제_실패() {
	// 	// given
	// 	List<OrderTableResponse> 주문_테이블_목록 = 주문_테이블_등록되어_있음(
	// 		OrderTableFixture.주문_테이블(), OrderTableFixture.주문_테이블());
	//
	// 	// given
	// 	ExtractableResponse<Response> 등록_응답 = step.등록_요청(주문_테이블_그룹);
	// 	TableGroupResponse 등록된_주문_테이블 = step.등록됨(등록_응답);
	//
	// 	given
	// 	orders.주문_등록되어_있음();
	// 	when
	// 	ExtractableResponse<Response> 해제_요청_응답 = 테이블_그룹_해제_요청(그룹_테이블);
	// 	then
	// 	테이블_그룹_해제_실패함(해제_요청_응답);
	// }

	private List<OrderTableResponse> 주문_테이블_등록되어_있음(OrderTableRequest ...orderTableRequests) {
		List<ExtractableResponse<Response>> responses = Arrays.stream(orderTableRequests)
			.map(orderTables::등록_요청)
			.collect(Collectors.toList());
		return responses.stream()
			.map(orderTables::등록됨)
			.collect(Collectors.toList());
	}

	private void 테이블_그룹_해제됨(ExtractableResponse<Response> 해제_요청_응답) {
		step.삭제됨(해제_요청_응답);
	}

	private ExtractableResponse<Response> 테이블_그룹_해제_요청(TableGroupResponse tableGroupResponse) {
		return step.테이블_그룹_해제요청(tableGroupResponse);
	}
}
