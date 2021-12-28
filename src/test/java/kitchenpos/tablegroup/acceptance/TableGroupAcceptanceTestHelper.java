package kitchenpos.tablegroup.acceptance;

import static org.assertj.core.api.Assertions.*;

import java.util.Arrays;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class TableGroupAcceptanceTestHelper {

    public static ExtractableResponse<Response> 단체_좌석_생성_요청(OrderTable... orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(orderTables));

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tableGroup)
            .when().post("/api/table-groups")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 단체_좌석_해제_요청(Long id) {
        return RestAssured
            .given().log().all()
            .when().delete("/api/table-groups/{tableGroupId}", id)
            .then().log().all().extract();
    }

    public static void 단쳬_좌석_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 단쳬_좌석_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
