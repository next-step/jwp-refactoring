package kitchenpos.tablegroup.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.HttpStatusAssertion;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceTestSupport extends AcceptanceTest {
    public ExtractableResponse<Response> 단체_지정_생성_요청(TableGroupRequest params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public ExtractableResponse<Response> 단체_지정_삭제_요청(ExtractableResponse<Response> createResponse) {
        return RestAssured
                .given().log().all()
                .when().delete(createResponse.header("Location"))
                .then().log().all()
                .extract();
    }

    public void 단체_지정_생성_완료(ExtractableResponse<Response> response) {
        HttpStatusAssertion.CREATED(response);
    }

    public void 단체_지정_삭제_완료(ExtractableResponse<Response> response) {
        HttpStatusAssertion.NO_CONTENT(response);
    }
}
