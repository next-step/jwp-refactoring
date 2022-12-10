package kitchenpos.acceptance;

import java.util.Arrays;
import java.util.List;
import java.util.function.ToLongFunction;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest2;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("주문 테이블 관리")
class OrderTableAcceptanceTest extends AcceptanceTest2<OrderTable> {

	static final String TABLES_REQUEST_PATH = "/api/tables";
	static final String EMPTY_REQUEST_PATH = "/api/tables/{orderTableId}/empty";
	static final String NUMBER_OF_GUESTS_REQUEST_PATH = "/api/tables/{orderTableId}/number-of-guests";

	OrderTable 주문_테이블;
	Long 테이블_아이디;

	TableGroupAcceptanceTest tableGroups;

	/**
	 * Feature: 주문 테이블 관리 기능
	 * Background
	 *   When 주문 테이블 등록을 요청하면
	 *   Then 주문 테이블 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		tableGroups = new TableGroupAcceptanceTest();

		주문_테이블 = 주문_테이블();
		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(주문_테이블);
		테이블_아이디 = 등록됨(등록_요청_응답).getId();
		주문_테이블.setId(테이블_아이디);
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
		주문_테이블.setEmpty(false);
		ExtractableResponse<Response> 수정_요청_응답 = 빈_주문_테이블_수정_요청(주문_테이블);
		// then
		테이블_수정됨(수정_요청_응답, 주문_테이블);

		// when
		int 변경된_손님_수 = 주문_테이블().getNumberOfGuests() + 1;
		주문_테이블.setNumberOfGuests(변경된_손님_수);
		수정_요청_응답 = 손님_수_수정_요청(주문_테이블);
		// then
		테이블_수정됨(수정_요청_응답, 주문_테이블);
	}

	/**
	 * When 주문 테이블이 존재하지 않을 경우
	 * Then 주문 테이블 수정에 실패한다
	 */
	@Test
	void 주문_테이블이_존재하지_않음() {
		// when
		OrderTable 새_주문_테이블 = 주문_테이블();
		새_주문_테이블.setId(-1L);
		ExtractableResponse<Response> 수정_응답 = 손님_수_수정_요청(새_주문_테이블);
		// then
		수정_실패함(수정_응답);
	}

	/**
	 * When 방문한 손님 수가 0명 미만일 경우
	 * Then 주문 테이블 수정에 실패한다
	 */
	@Test
	void 방문_손님_수가_0명_미만() {
		// when
		int 변경된_손님_수 = -1;
		주문_테이블.setNumberOfGuests(변경된_손님_수);
		ExtractableResponse<Response> 수정_응답 = 손님_수_수정_요청(주문_테이블);

		// when
		수정_실패함(수정_응답);
	}

	/**
	 * When 빈 테이블이 아닌 테이블의 손님 수 변경을 요청하면
	 * Then 주문 테이블 수정에 실패한다
	 */
	@Test
	void 빈_테이블이_아님() {
		int 변경된_손님_수 = 주문_테이블.getNumberOfGuests() + 1;
		주문_테이블.setNumberOfGuests(변경된_손님_수);

		ExtractableResponse<Response> 수정_응답 = 손님_수_수정_요청(주문_테이블);

		수정_실패함(수정_응답);
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
		List<OrderTable> 주문_테이블_목록 = 테이블_등록됨(주문_테이블(), 주문_테이블());

		// given
		TableGroup 테이블_그룹 = tableGroups.그룹_테이블_등록되어_있음(주문_테이블_목록);

		// when
		OrderTable 수정_주문_테이블 = 테이블_그룹.getOrderTables().get(0);
		수정_주문_테이블.setEmpty(false);
		ExtractableResponse<Response> 수정_응답 = 빈_주문_테이블_수정_요청(수정_주문_테이블);

		// then
		수정_실패함(수정_응답);
	}

	/**
	 * Given 주문 상태가 '조리중' 또는 '식사중'인 주문이 등록되어 있을 때
	 * When 주문 테이블을 빈 테이블로 변경 요청할 경우
	 * Then 주문 테이블 수정에 실패한다
	 */
	@Test
	@Disabled
	void 주문_상태가_조리중_이거나_식사중임() {
	}

	private void 테이블_수정됨(ExtractableResponse<Response> 수정_요청_응답, OrderTable 주문_테이블) {
		수정됨(수정_요청_응답, 주문_테이블, OrderTable::isEmpty);
		수정됨(수정_요청_응답, 주문_테이블, OrderTable::getNumberOfGuests);
	}

	private ExtractableResponse<Response> 빈_주문_테이블_수정_요청(OrderTable orderTable) {
		return 수정_요청(EMPTY_REQUEST_PATH, orderTable);
	}

	private ExtractableResponse<Response> 손님_수_수정_요청(OrderTable orderTable) {
		return 수정_요청(NUMBER_OF_GUESTS_REQUEST_PATH, orderTable);
	}

	public List<OrderTable> 테이블_등록됨(OrderTable... orderTables) {
		return Arrays.stream(orderTables)
			.map(orderTable -> 등록됨(등록_요청(orderTable)))
			.collect(Collectors.toList());
	}

	public static OrderTable 주문_테이블() {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(0);
		orderTable.setEmpty(true);
		return orderTable;
	}

	@Override
	protected String getRequestPath() {
		return TABLES_REQUEST_PATH;
	}

	@Override
	protected ToLongFunction<OrderTable> idExtractor() {
		return OrderTable::getId;
	}

	@Override
	protected Class<OrderTable> getDomainClass() {
		return OrderTable.class;
	}
}
