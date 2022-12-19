package kitchenpos.tablegroup.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceTest.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.DynamicTest.*;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicNode;
import org.junit.jupiter.api.TestFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.AcceptanceTest;
import kitchenpos.exception.CustomErrorResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@DisplayName("주문 테이블 단체지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {
	@DisplayName("단체지정 통합 테스트")
	@TestFactory
	Stream<DynamicNode> tableGroup() {
		return Stream.of(
			dynamicTest("단체 지정 한다.", () -> {
				// given
				Long 빈_테이블_A_ID = 테이블_생성됨(2, true);
				Long 빈_테이블_B_ID = 테이블_생성됨(2, true);
				// when
				ExtractableResponse<Response> response = 단체지정_요청(단체지정_생성(빈_테이블_A_ID, 빈_테이블_B_ID));
				// then
				단체지정_정상_생성됨(response);
			}),
			dynamicTest("테이블이 2개 미만이면 단체지정 할 수 없다.", () -> {
				// given
				Long 빈_테이블_ID = 테이블_생성됨(2, true);
				// when
				ExtractableResponse<Response> response = 단체지정_요청(단체지정_생성(빈_테이블_ID));
				// then
				요청_실패됨_잘못된_요청(response);
			}),
			dynamicTest("등록되지 않은 테이블이 있으면 단체지정 할 수 없다.", () -> {
				// given
				Long 빈_테이블_ID = 테이블_생성됨(2, true);
				Long 미등록_주문테이블_ID = Long.MAX_VALUE;
				// when
				ExtractableResponse<Response> response = 단체지정_요청(단체지정_생성(빈_테이블_ID, 미등록_주문테이블_ID));
				// then
				요청_실패됨_엔티티_찾을_수_없음(response);
			}),
			dynamicTest("채워진 테이블은 단체지정 할 수 없다.", () -> {
				// given
				Long 빈_테이블_ID = 테이블_생성됨(2, true);
				Long 채워진_테이블_ID = 테이블_생성됨(2, false);
				// when
				ExtractableResponse<Response> response = 단체지정_요청(단체지정_생성(빈_테이블_ID, 채워진_테이블_ID));
				// then
				요청_실패됨_잘못된_요청(response);
			}),
			dynamicTest("이미 단체지정된 테이블은 단체지정 할 수 없다.", () -> {
				// given
				Long 빈_테이블_A_ID = 테이블_생성됨(2, true);
				Long 빈_테이블_B_ID = 테이블_생성됨(2, true);
				단체지정됨(빈_테이블_A_ID, 빈_테이블_B_ID);
				// when
				ExtractableResponse<Response> response = 단체지정_요청(단체지정_생성(빈_테이블_A_ID, 빈_테이블_B_ID));
				// then
				요청_실패됨_잘못된_요청(response);
			}),
			dynamicTest("단체지정을 취소한다.", () -> {
				// given
				Long 빈_테이블_A_ID = 테이블_생성됨(2, true);
				Long 빈_테이블_B_ID = 테이블_생성됨(2, true);
				Long 단체지정_ID = 단체지정됨(빈_테이블_A_ID, 빈_테이블_B_ID).getId();
				// when
				ExtractableResponse<Response> response = 단체지정_해제_요청(단체지정_ID);
				// then
				단체지정_정상_해제됨(response);
			})
		);
	}

	private static TableGroupRequest 단체지정_생성(Long... 주문테이블_ID) {
		return TableGroupRequest.of(Arrays.asList(주문테이블_ID));
	}

	public static TableGroupResponse 단체지정됨(Long... 주문테이블_ID) {
		return 단체지정_요청(단체지정_생성(주문테이블_ID)).as(TableGroupResponse.class);
	}

	public static ExtractableResponse<Response> 단체지정_요청(TableGroupRequest tableGroupRequest) {
		return RestAssured
			.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(tableGroupRequest)
			.when().post("/api/table-groups/")
			.then().log().all()
			.extract();
	}

	public static ExtractableResponse<Response> 단체지정_해제_요청(Long id) {
		return RestAssured
			.given().log().all()
			.when().delete("/api/table-groups/{id}", id)
			.then().log().all()
			.extract();
	}

	private void 단체지정_정상_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}

	private void 요청_실패됨_잘못된_요청(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
	}

	private void 요청_실패됨_엔티티_찾을_수_없음(ExtractableResponse<Response> response) {
		CustomErrorResponse customErrorResponse = response.as(CustomErrorResponse.class);
		assertAll(
			() -> assertThat(customErrorResponse.getStatusCode()).isEqualTo(404),
			() -> assertThat(customErrorResponse.getStatusName()).isEqualTo("NOT_FOUND")
		);
	}

	private void 단체지정_정상_해제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

}
