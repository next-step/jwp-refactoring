package kitchenpos.acceptance;

import static kitchenpos.acceptance.OrderTableAcceptanceTest.주문_테이블;
import static org.assertj.core.util.Lists.newArrayList;

import java.util.List;
import java.util.function.ToLongFunction;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

@DisplayName("테이블 그룹 관리")
class TableGroupAcceptanceTest extends AcceptanceTest<TableGroup> {

	static final String GROUP_REQUEST_PATH = "/api/table-groups";
	static final String UNGROUP_REQUEST_PATH = "/api/table-groups/{tableGroupId}";

	List<OrderTable> 주문_테이블_목록;
	OrderTableAcceptanceTest orderTables;

	/**
	 * Feature: 테이블 그룹 관리 기능
	 * Background
	 *   when 주문 테이블 등록을 요청하면
	 *   then 주문 테이블 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		orderTables = new OrderTableAcceptanceTest();
		주문_테이블_목록 = orderTables.테이블_등록됨(주문_테이블(), 주문_테이블());
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
		TableGroup 주문_테이블_그룹 = 주문_테이블_그룹(주문_테이블_목록);
		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(주문_테이블_그룹);
		// then
		주문_테이블_그룹 = 등록됨(등록_요청_응답);

		// when
		ExtractableResponse<Response> 해제_요청_응답 = 테이블_그룹_해제_요청(주문_테이블_그룹);
		테이블_그룹_해제됨(해제_요청_응답);
	}

	/**
	 * Given 주문 테이블이 생성되어 있지 않을경우
	 * When 테이블 그룹 생성을 요청하면
	 * Then 테이블 그룹 생성에 실패한다
	 */
	@Test
	void 주문_테이블이_존재하지_않음() {
		OrderTable 새_주문_테이블 = 주문_테이블();
		TableGroup 주문_테이블_그룹 = 주문_테이블_그룹(newArrayList(새_주문_테이블));

		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(주문_테이블_그룹);

		등록_실패함(등록_요청_응답);
	}

	/**
	 * Given 주문 테이블 목록이 2개 미만일 경우
	 * When 테이블 그룹 생성을 요청하면
	 * Then 테이블 그룹 생성에 실패한다
	 */
	@Test
	void 주문_테이블_목록이_2개_미만() {
		TableGroup 주문_테이블_그룹 = 주문_테이블_그룹(newArrayList(주문_테이블()));

		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(주문_테이블_그룹);

		등록_실패함(등록_요청_응답);
	}

	/**
	 * Given 주문 테이블 그룹이 생성되어 있고
	 * Given 주문 테이블의 상태가 '조리중'이거나 '식사중'일 때
	 * When 테이블 그룹 해제를 요청하면
	 * Then 테이블 그룹 해제가 실패한다.
	 */
	@Disabled
	@Test
	void 주문_테이블_그룹_해제_실패() {
	}

	public TableGroup 그룹_테이블_등록되어_있음(List<OrderTable> orderTables) {
		TableGroup 테이블_그룹 = 주문_테이블_그룹(orderTables);
		ExtractableResponse<Response> 등록_요청_응답 = 등록_요청(테이블_그룹);
		return 등록됨(등록_요청_응답);
	}

	private TableGroup 주문_테이블_그룹(List<OrderTable> orderTables) {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);
		return tableGroup;
	}

	private void 테이블_그룹_해제됨(ExtractableResponse<Response> 해제_요청_응답) {
		super.삭제됨(해제_요청_응답);
	}

	private ExtractableResponse<Response> 테이블_그룹_해제_요청(TableGroup 주문_테이블_그룹) {
		return 삭제_요청(UNGROUP_REQUEST_PATH, 주문_테이블_그룹.getId());
	}

	@Override
	protected String getRequestPath() {
		return GROUP_REQUEST_PATH;
	}

	@Override
	protected ToLongFunction<TableGroup> idExtractor() {
		return TableGroup::getId;
	}

	@Override
	protected Class<TableGroup> getDomainClass() {
		return TableGroup.class;
	}
}
