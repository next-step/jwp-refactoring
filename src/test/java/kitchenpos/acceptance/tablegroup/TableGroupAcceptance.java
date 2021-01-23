package kitchenpos.acceptance.tablegroup;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.acceptance.util.AcceptanceTest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

public class TableGroupAcceptance extends AcceptanceTest {

	public static final String TABLE_GROUP_REQUEST_URL = "/api/table-groups";

	public static ExtractableResponse<Response> 단체_지정_등록_요청(TableGroupRequest request) {
		return RestAssured
			.given().log().all()
			.body(request)
			.contentType(MediaType.APPLICATION_JSON_VALUE)
			.when().post(TABLE_GROUP_REQUEST_URL)
			.then().log().all().extract();
	}

	public static ExtractableResponse<Response> 단체_지정_해제_요청(TableGroupResponse expected) {
		return RestAssured
			.given().log().all()
			.when().delete(TABLE_GROUP_REQUEST_URL + "/{tableGroupId}", expected.getId())
			.then().log().all().extract();
	}

	public static void 단체_지정_등록됨(ExtractableResponse<Response> response) {
		assertAll(
			() -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
			() -> assertThat(response.as(TableGroupResponse.class)).isNotNull(),
			() -> assertThat(response.as(TableGroupResponse.class).getId()).isNotNull(),
			() -> assertThat(response.as(TableGroupResponse.class).getCreatedDate()).isNotNull()
		);
	}

	public static void 단체_지정_해제됨(ExtractableResponse<Response> response) {
		assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
	}
}
