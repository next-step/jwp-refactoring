package kitchenpos.order.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;
import java.util.Objects;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.order.dto.TableGroupRequest;
import kitchenpos.order.dto.TableGroupResponse;

public class TableGroupAcceptanceTest extends AcceptanceTest {
	@DisplayName("단체 지정 요청")
	@Test
	void create() {
		TableAcceptanceTest.테이블_생성_요청(0, true);
		TableAcceptanceTest.테이블_생성_요청(0, true);
		ExtractableResponse<Response> createResponse = 단체_지정_요청(new OrderTableRequest(1L), new OrderTableRequest(2L));

		단체_지정_성공(createResponse);
	}

	@DisplayName("2개 미만의 테이블 단체 지정시 실패")
	@Test
	void createWhenLessThanTwo() {
		ExtractableResponse<Response> createResponse = 단체_지정_요청(new OrderTableRequest(1L));

		단체_지정_실패(createResponse);
	}

	@DisplayName("이미 단체 지정이 된 테이블 지정시 실패")
	@Test
	void createWhenAlreadyGrouping() {
		TableAcceptanceTest.테이블_생성_요청(0, true);
		TableAcceptanceTest.테이블_생성_요청(0, true);
		단체_지정됨(new OrderTableRequest(1L), new OrderTableRequest(2L));

		ExtractableResponse<Response> createResponse = 단체_지정_요청(new OrderTableRequest(1L), new OrderTableRequest(2L));

		단체_지정_실패(createResponse);
	}

	@DisplayName("비어 있지 않은 테이블 단체 지정시 실패")
	@Test
	void createWhenOrderTables() {

	}

	@DisplayName("단체 지정 해제")
	@Test
	void ungroup() {
		TableAcceptanceTest.테이블_생성_요청(0, true);
		TableAcceptanceTest.테이블_생성_요청(0, true);
		단체_지정_요청(new OrderTableRequest(1L), new OrderTableRequest(1L));

		ExtractableResponse<Response> deleteResponse = 단체_지정_해제_요청(
			단체_지정_요청(new OrderTableRequest(1L), new OrderTableRequest(2L)));

		단체_지정_해제_성공(deleteResponse);
	}

	@DisplayName("완료되지 않은 테이블이 있을시 해제 실패")
	@Test
	void ungroupWhenNotCompletion() {

	}

	public static void 단체_지정됨(OrderTableRequest... orderTableRequests) {
		단체_지정_성공(단체_지정_요청(orderTableRequests));
	}

	public static ExtractableResponse<Response> 단체_지정_요청(OrderTableRequest... orderTableRequests) {
		TableGroupRequest tableGroupRequest = new TableGroupRequest(Arrays.asList(orderTableRequests));

		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tableGroupRequest)
			.when().post("/api/table-groups")
			.then().log().all()
			.extract();
	}

	public static void 단체_지정_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
		TableGroupResponse tableGroupResponse = response.as(TableGroupResponse.class);
		assertThat(tableGroupResponse.getOrderTables())
			.map(OrderTableResponse::getTableGroupId)
			.allMatch(Objects::nonNull);
	}

	public static void 단체_지정_실패(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public static ExtractableResponse<Response> 단체_지정_해제_요청(ExtractableResponse<Response> response) {
		return RestAssured.given().log().all()
			.when().delete(response.header("Location"))
			.then().log().all()
			.extract();
	}

	public static void 단체_지정_해제_성공(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
