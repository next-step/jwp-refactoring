package kitchenpos.acceptance;

import static kitchenpos.acceptance.OrderTableAcceptanceTest.주문_테이블;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.utils.RestAssuredUtils;

@DisplayName("테이블 그룹 관리")
class TableGroupAcceptanceTest extends AcceptanceTest {

	static final String GROUP_REQUEST_PATH = "/api/table-groups";
	static final String UNGROUP_REQUEST_PATH = "/api/table-groups/{tableGroupId}";

	List<OrderTable> 주문_테이블_목록;
	TableGroup 테이블_그룹;

	/**
	 * Feature: 테이블 그룹 관리 기능
	 * Background
	 *   when 주문 테이블 등록을 요청하면
	 *   then 주문 테이블 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		주문_테이블_목록 = 주문_테이블_목록_생성();
	}

	/**
	 * Given 주문 테이블이 생성되어 있고
	 * When 테이블 그룹 생성을 요청하면
	 * Then 테이블 그룹 생성에 성공한다
	 */
	@Test
	void 테이블_그룹_생성() {
		TableGroup 주문_테이블_그룹 = 테이블_그룹(주문_테이블_목록);

		ExtractableResponse<Response> 생성_응답 = 그룹_테이블_등록_요청(주문_테이블_그룹);

		그룹_테이블_등록됨(생성_응답);
	}

	/**
	 * Given 주문 테이블이 생성되어 있지 않을경우
	 * When 테이블 그룹 생성을 요청하면
	 * Then 테이블 그룹 생성에 실패한다
	 */
	@Test
	void 주문_테이블이_존재하지_않음() {
		OrderTable 주문_테이블 = 주문_테이블();
		TableGroup 주문_테이블_그룹 = 테이블_그룹(Lists.newArrayList(주문_테이블));

		ExtractableResponse<Response> 생성_응답 = 그룹_테이블_등록_요청(주문_테이블_그룹);

		그룹_테이블_등록_실패함(생성_응답);
	}

	/**
	 * Given 주문 테이블 목록이 2개 미만일 경우
	 * When 테이블 그룹 생성을 요청하면
	 * Then 테이블 그룹 생성에 실패한다
	 */
	@Test
	void 주문_테이블_목록이_빈배열() {
		TableGroup 주문_테이블_그룹 = 테이블_그룹(Lists.newArrayList(주문_테이블()));

		ExtractableResponse<Response> 생성_응답 = 그룹_테이블_등록_요청(주문_테이블_그룹);

		그룹_테이블_등록_실패함(생성_응답);
	}

	private void 그룹_테이블_등록_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isNotEqualTo(HttpStatus.OK.value());
	}

	public static Long 그룹_테이블_등록됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		Long tableGroupId = response.body().as(TableGroup.class).getId();
		assertThat(tableGroupId).isNotNull();
		return tableGroupId;
	}

	private List<OrderTable> 주문_테이블_목록_생성() {
		List<OrderTable> orderTables = new ArrayList<>();
		orderTables.addAll(
			Lists.newArrayList(주문_테이블_등록_요청(),
							   주문_테이블_등록_요청(),
							   주문_테이블_등록_요청()));
		return orderTables;
	}

	private static OrderTable 주문_테이블_등록_요청() {
		OrderTable orderTable = 주문_테이블();
		Long tableId = OrderTableAcceptanceTest.테이블_등록_요청(orderTable);
		OrderTableAcceptanceTest.테이블_등록됨(tableId);
		orderTable.setId(tableId);
		return orderTable;
	}

	public static ExtractableResponse<Response> 그룹_테이블_등록_요청(TableGroup tableGroup) {
		return RestAssuredUtils.post(GROUP_REQUEST_PATH, tableGroup);
	}

	public static TableGroup 테이블_그룹(List<OrderTable> orderTables) {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);
		return tableGroup;
	}

	public static TableGroup 그룹_테이블_등록되어_있음(List<OrderTable> orderTables) {
		TableGroup 테이블_그룹 = 테이블_그룹(orderTables);
		ExtractableResponse<Response> 테이블_그룹_생성_응답 = 그룹_테이블_등록_요청(테이블_그룹);
		그룹_테이블_등록됨(테이블_그룹_생성_응답);
		return 테이블_그룹_생성_응답.body().as(TableGroup.class);
	}

}
