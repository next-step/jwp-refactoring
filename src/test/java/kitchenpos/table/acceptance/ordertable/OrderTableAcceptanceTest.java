package kitchenpos.table.acceptance.ordertable;

import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.acceptance.OrderAcceptanceTestStep;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.ui.dto.OrderResponse;
import kitchenpos.table.acceptance.tablegroup.TableGroupAcceptanceTestStep;
import kitchenpos.table.acceptance.tablegroup.TableGroupFixture;
import kitchenpos.table.ui.dto.OrderTableRequest;
import kitchenpos.table.ui.dto.OrderTableResponse;

@DisplayName("주문 테이블 관리")
class OrderTableAcceptanceTest extends AcceptanceTest {

	OrderTableResponse 주문_테이블;

	OrderTableAcceptanceTestStep step = new OrderTableAcceptanceTestStep();
	TableGroupAcceptanceTestStep tableGroups = new TableGroupAcceptanceTestStep();
	OrderAcceptanceTestStep orders = new OrderAcceptanceTestStep();

	/**
	 * Feature: 주문 테이블 관리 기능
	 * Background
	 * When 주문 테이블 등록을 요청하면
	 * Then 주문 테이블 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		OrderTableRequest 주문_테이블 = OrderTableFixture.주문_테이블();
		ExtractableResponse<Response> 등록_요청_응답 = step.등록_요청(주문_테이블);
		this.주문_테이블 = step.등록됨(등록_요청_응답);
	}

	/**
	 * Given 수정할 테이블이 주문 불가 테이블이고
	 * When 주문 테이블을 주문 불가 테이블로 수정을 요청하면
	 * Then 주문 테이블 수정에 성공한다
	 */
	@Test
	void 주문_테이블_주문_불가로_수정() {
		// given
		OrderTableRequest 주문_불가_테이블 = OrderTableFixture.주문_불가_테이블(주문_테이블);
		// when
		ExtractableResponse<Response> 수정_요청_응답 = step.빈_주문_테이블_수정_요청(주문_테이블.getId(), 주문_불가_테이블);
		// then
		step.수정됨(수정_요청_응답);
	}

	/**
	 * Given 수정할 손님의 수가 10명 이고
	 * When 주문 테이블의 손님 수 수정을 요청하면
	 * Then 주문 테이블 손님 수 수정에 성공한다
	 */
	@Test
	void 주문_테이블_손님_수_수정() {
		// given
		OrderTableRequest 수정된_주문_테이블 = OrderTableFixture.주문_테이블(10);
		// when
		ExtractableResponse<Response> 수정_요청_응답 = step.손님_수_수정_요청(주문_테이블.getId(), 수정된_주문_테이블);
		// then
		step.수정됨(수정_요청_응답);
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
	@Test
	void 주문_테이블이_그룹_테이블에_존재함() {
		// given
		List<OrderTableResponse> 주문_테이블_목록 = step.등록되어_있음(Lists.newArrayList(OrderTableFixture.주문_테이블(),
																			 OrderTableFixture.주문_테이블()));

		// given
		tableGroups.등록되어_있음(TableGroupFixture.주문_테이블_그룹(주문_테이블_목록));

		// when
		OrderTableResponse 수정_주문_테이블 = 주문_테이블_목록.get(0);
		OrderTableRequest 수정할_테이블 = OrderTableFixture.주문_테이블(수정_주문_테이블.getNumberOfGuests(), true);
		ExtractableResponse<Response> 수정_응답 = step.빈_주문_테이블_수정_요청(수정_주문_테이블.getId(), 수정할_테이블);

		// then
		step.수정_실패함(수정_응답);
	}

	/**
	 * Given 주문 테이블이 등록되어 있고
	 * Given 주문 테이블 그룹에 해당 주문 테이블이 등록되어 있고
	 * Given 메뉴그룹이 등록되어 있고
	 * Given 메뉴가 등록되어 있고
	 * Given 주문 상태가 '조리중' 또는 '식사중'인 주문이 등록되어 있을 때
	 * When 주문 테이블을 빈 테이블로 변경 요청할 경우
	 * Then 주문 테이블 수정에 실패한다
	 */
	@Test
	void 주문_상태가_조리중_이거나_식사중임() {
		// given
		List<OrderTableResponse> 주문_테이블_목록 = step.등록되어_있음(Lists.newArrayList(OrderTableFixture.주문_테이블(),
																			 OrderTableFixture.주문_테이블()));
		tableGroups.등록되어_있음(TableGroupFixture.주문_테이블_그룹(주문_테이블_목록));
		OrderTableResponse 주문_테이블 = 주문_테이블_목록.get(0);

		OrderResponse 주문 = orders.등록되어_있음(주문_테이블);

		orders.주문_상태_변경_요청(주문.getId(), OrderStatus.MEAL);

		// when
		OrderTableRequest 빈_테이블 = OrderTableFixture.주문_테이블(10, false);
		ExtractableResponse<Response> 수정_응답 = step.빈_주문_테이블_수정_요청(주문_테이블.getId(), 빈_테이블);

		// then
		step.수정_실패함(수정_응답);
	}
}
