package kitchenpos.tablegroup;

import static kitchenpos.table.TableAcceptanceStaticTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

public class TableGroupAcceptanceStaticTest {

	public static final String TABLE_GROUP_PATH = "/api/table-groups";

	public static void 테이블_그룹_해체에_실패함(ExtractableResponse<Response> response, int status) {
		assertThat(response.statusCode()).isEqualTo(status);
	}

	public static TableGroupResponse 테이블_그룹이_생성_되어있음(TableGroupRequest params) {
		return 테이블_그룹_생성_요청(params).as(TableGroupResponse.class);
	}

	public static ExtractableResponse<Response> 테이블_그룹_해체_요청(Long id) {
		return RestAssured.given().log().all()
			.when()
			.delete(TABLE_GROUP_PATH + SLASH + id)
			.then().log().all()
			.extract();
	}

	public static void 테이블_그룹이_해체됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	public static void 테이블_그룹_생성_실패함(ExtractableResponse<Response> response, int status) {
		assertThat(response.statusCode()).isEqualTo(status);
	}

	public static TableGroupRequest 테이블_그룹_요청값_생성(List<OrderTableResponse> orderTables) {
		List<Long> orderTableIds = orderTables.stream()
			.map(OrderTableResponse::getId)
			.collect(Collectors.toList());
		return TableGroupRequest.of(orderTableIds);
	}

	public static ExtractableResponse<Response> 테이블_그룹_생성_요청(TableGroupRequest params) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when()
			.post(TABLE_GROUP_PATH)
			.then().log().all()
			.extract();
	}

	public static void 테이블_그룹이_생성됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
	}
}
