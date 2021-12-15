package kitchenpos.domain;

import static kitchenpos.domain.TableAcceptanceStaticTest.*;
import static org.assertj.core.api.Assertions.*;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;

public class TableGroupAcceptanceStaticTest {

	public static final String TABLE_GROUP_PATH = "/api/table-groups";

	public static void 테이블_그룹_해체에_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public static TableGroup 테이블_그룹이_생성_되어있음(TableGroup params) {
		return 테이블_그룹_생성_요청(params).as(TableGroup.class);
	}

	public static ExtractableResponse<Response> 테이블_그룹_해체_요청(TableGroup params) {
		return RestAssured.given().log().all()
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.body(params)
			.when()
			.delete(TABLE_GROUP_PATH + SLASH + params.getId())
			.then().log().all()
			.extract();
	}

	public static void 테이블_그룹이_해체됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}

	public static void 테이블_그룹_생성_실패함(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
	}

	public static TableGroup 테이블_그룹_요청값_생성(List<OrderTable> orderTables) {
		TableGroup tableGroup = new TableGroup();
		tableGroup.setOrderTables(orderTables);
		return tableGroup;
	}

	public static ExtractableResponse<Response> 테이블_그룹_생성_요청(TableGroup params) {
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
