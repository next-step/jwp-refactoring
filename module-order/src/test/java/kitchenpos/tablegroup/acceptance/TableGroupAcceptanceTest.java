package kitchenpos.tablegroup.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.ArrayList;

import org.assertj.core.api.AbstractIntegerAssert;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;

@DisplayName("테이블그룹 기능 인수테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

	@Test
	@DisplayName("테이블그룹 생성 테스트")
	public void createTableGroupTest() {
		//given
		OrderTableRequest tableRequest = new OrderTableRequest(1L);
		OrderTableRequest otherTableRequest = new OrderTableRequest(2L);
		ArrayList<OrderTableRequest> orderTableRequests = Lists.newArrayList(tableRequest, otherTableRequest);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

		//when
		ExtractableResponse<Response> response = 테이블그룹_생성_요청(tableGroupRequest);

		//then
		테이블그룹_생성_성공(response);
	}

	@Test
	@DisplayName("2개 미만의 테이블개수로 테이블그룹 생성 요청해서 실패")
	public void createTableGroupFailOrderTableLessThanOneTest() {
		//given
		TableGroupRequest tableGroupRequest = new TableGroupRequest(Lists.emptyList());

		//when
		ExtractableResponse<Response> response = 테이블그룹_생성_요청(tableGroupRequest);

		//then
		테이블그룹관련_요청_실패(response, "2개 이상의 테이블만 그룹생성이 가능합니다");
	}

	@Test
	@DisplayName("테이블이 사용중이거나 그룹화되어서 생성 요청 실패")
	public void createTableGroupFailOrderTableIsUseOrIsGroupedTest() {
		//given
		OrderTableRequest tableRequest = new OrderTableRequest(4L);
		OrderTableRequest otherTableRequest = new OrderTableRequest(8L);
		ArrayList<OrderTableRequest> orderTableRequests = Lists.newArrayList(tableRequest, otherTableRequest);
		TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);

		//when
		ExtractableResponse<Response> response = 테이블그룹_생성_요청(tableGroupRequest);

		//then
		테이블그룹관련_요청_실패(response, "이미 사용중이거나 그룹화된 테이블은 그룹생성 할 수 없습니다");
	}

	@Test
	@DisplayName("테이블그룹 해제 테스트")
	public void ungroupTableGroupTest() {
		//given
		//when
		ExtractableResponse<Response> response = 테이블그룹_해제_요청(1L);

		//then
		테이블그룹_해제_성공(response);
	}

	@Test
	@DisplayName("테이블의 주문이 계산완료되지않아서 그룹 해제 실패")
	public void ungroupFailOrderIsNotCompletion() {
		//given
		//when
		ExtractableResponse<Response> response = 테이블그룹_해제_요청(2L);

		//then
		테이블그룹관련_요청_실패(response, "아직 테이블의 주문이 계산완료되지 않았습니다");
	}

	private ExtractableResponse<Response> 테이블그룹_해제_요청(long tableGroupId) {
		return RestAssured.given().log().all()
			.when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
			.then().log().all()
			.extract();
	}

	private void 테이블그룹_생성_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		assertThat(response.header("Location")).isEqualTo("/api/table-groups/3");
	}

	private ExtractableResponse<Response> 테이블그룹_생성_요청(TableGroupRequest tableGroupRequest) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tableGroupRequest)
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
	}

	private AbstractIntegerAssert<?> 테이블그룹_해제_성공(ExtractableResponse<Response> response) {
		return assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	private void 테이블그룹관련_요청_실패(ExtractableResponse<Response> response, String message) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
		assertThat(response.body().asString()).isEqualTo(message);
	}
}
