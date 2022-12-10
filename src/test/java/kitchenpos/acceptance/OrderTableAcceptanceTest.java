package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableGroupAcceptanceTest.그룹_테이블_등록_요청;
import static kitchenpos.acceptance.TableGroupAcceptanceTest.그룹_테이블_등록됨;
import static kitchenpos.acceptance.TableGroupAcceptanceTest.테이블_그룹;
import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;
import java.util.stream.Collectors;

import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.utils.RestAssuredUtils;

@DisplayName("주문 테이블 관리")
class OrderTableAcceptanceTest extends AcceptanceTest {

	static final String TABLE_REQUEST_PATH = "/api/tables";
	static final String EMPTY_TABLE_REQUEST_PATH = "/api/tables/{orderTableId}/empty";
	static final String NUMBER_OF_GUESTS_REQUEST_PATH = "/api/tables/{orderTableId}/number-of-guests";

	OrderTable 주문_테이블;
	Long 테이블_아이디;

	/**
	 * Feature: 주문 테이블 관리 기능
	 * Background
	 *   When 주문 테이블 등록을 요청하면
	 *   Then 주문 테이블 등록에 성공한다
	 */
	@BeforeEach
	void setup() {
		주문_테이블 = 주문_테이블();
		테이블_아이디 = 테이블_등록_요청(주문_테이블);
		테이블_등록됨(테이블_아이디);
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
		주문_테이블.setEmpty(false);
		빈_주문_테이블_수정_요청(주문_테이블);

		테이블_수정됨(주문_테이블);

		int 변경된_손님_수 = 주문_테이블().getNumberOfGuests() + 1;
		주문_테이블.setNumberOfGuests(변경된_손님_수);
		손님_수_수정_요청(주문_테이블);

		테이블_수정됨(주문_테이블);
	}

	/**
	 * When 주문 테이블이 존재하지 않을 경우
	 * Then 주문 테이블 수정에 실패한다
	 */
	@Test
	void 주문_테이블이_존재하지_않음() {
		OrderTable 새_주문_테이블 = 주문_테이블();
		새_주문_테이블.setId(-1L);
		ExtractableResponse<Response> 수정_응답 = 손님_수_수정_요청(새_주문_테이블);

		테이블_수정_실패함(수정_응답);
	}

	/**
	 * When 방문한 손님 수가 0명 미만일 경우
	 * Then 주문 테이블 수정에 실패한다
	 */
	@Test
	void 방문_손님_수가_0명_미만() {
		int 변경된_손님_수 = -1;
		주문_테이블.setNumberOfGuests(변경된_손님_수);

		ExtractableResponse<Response> 수정_응답 = 손님_수_수정_요청(주문_테이블);

		테이블_수정_실패함(수정_응답);
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

		테이블_수정_실패함(수정_응답);
	}

	/**
	 * Given 주문 테이블이 2개이상 등록하고
	 * Given 그룹 테이블에 해당 주문 테이블들을 생성하고
	 * When 주문 테이블을 빈 테이블로 수정 요청할 경우
	 * Then 주문 테이블 수정에 실패한다
	 */
	@Test
	void 주문_테이블이_그룹_테이블에_존재함() {
		List<OrderTable> 주문_테이블_목록
			= 테이블_등록_요청(Lists.newArrayList(주문_테이블(), 주문_테이블(), 주문_테이블()));

		TableGroup 테이블_그룹 = 테이블_그룹(주문_테이블_목록);
		ExtractableResponse<Response> 테이블_그룹_생성_응답 = 그룹_테이블_등록_요청(테이블_그룹);
		그룹_테이블_등록됨(테이블_그룹_생성_응답);

		OrderTable 수정_주문_테이블 = 주문_테이블_목록.get(0);
		수정_주문_테이블.setEmpty(false);
		ExtractableResponse<Response> 수정_응답 = 빈_주문_테이블_수정_요청(수정_주문_테이블);

		테이블_수정_실패함(수정_응답);
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

	private void 테이블_수정_실패함(ExtractableResponse<Response> 주문_테이블) {
		assertThat(주문_테이블.statusCode()).isNotEqualTo(HttpStatus.OK.value());
	}

	private ExtractableResponse<Response> 빈_주문_테이블_수정_요청(OrderTable orderTable) {
		return 주문_테이블_수정_요청(EMPTY_TABLE_REQUEST_PATH, orderTable);
	}

	private ExtractableResponse<Response> 손님_수_수정_요청(OrderTable orderTable) {
		return 주문_테이블_수정_요청(NUMBER_OF_GUESTS_REQUEST_PATH, orderTable);
	}

	private ExtractableResponse<Response> 주문_테이블_수정_요청(String path, OrderTable orderTable) {
		return RestAssuredUtils.put(path, orderTable.getId(), orderTable);
	}

	public static List<OrderTable> 테이블_등록_요청(List<OrderTable> orderTables) {
		return orderTables.stream()
			.peek(orderTable -> orderTable.setId(테이블_등록_요청(orderTable)))
			.collect(Collectors.toList());
	}

	public static Long 테이블_등록_요청(OrderTable orderTable) {
		ExtractableResponse<Response> 등록_응답 = RestAssuredUtils.post(TABLE_REQUEST_PATH, orderTable);
		assertThat(등록_응답.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		return 등록_응답.body().as(OrderTable.class).getId();
	}

	public static void 테이블_등록됨(Long 테이블_아이디) {
		List<OrderTable> orderTables = 테이블_목록_조회();

		assertThat(orderTables)
			.extracting(OrderTable::getId)
			.contains(테이블_아이디);
	}

	private static List<OrderTable> 테이블_목록_조회() {
		ExtractableResponse<Response> 목록_응답 = RestAssuredUtils.get(TABLE_REQUEST_PATH);

		assertThat(목록_응답.statusCode()).isEqualTo(HttpStatus.OK.value());
		return 목록_응답.body().as(new TypeRef<List<OrderTable>>() {});
	}

	private void 테이블_수정됨(OrderTable 주문_테이블) {
		List<OrderTable> orderTables = 테이블_목록_조회();
		OrderTable actualOrderTable = orderTables.stream()
			.filter(orderTable -> orderTable.getId().equals(주문_테이블.getId()))
			.findAny()
			.get();

		assertThat(actualOrderTable)
			.extracting(OrderTable::isEmpty)
			.isEqualTo(주문_테이블.isEmpty());

		assertThat(actualOrderTable)
			.extracting(OrderTable::getNumberOfGuests)
			.isEqualTo(주문_테이블.getNumberOfGuests());

	}

	public static OrderTable 주문_테이블() {
		OrderTable orderTable = new OrderTable();
		orderTable.setNumberOfGuests(0);
		orderTable.setEmpty(true);
		return orderTable;
	}
}
