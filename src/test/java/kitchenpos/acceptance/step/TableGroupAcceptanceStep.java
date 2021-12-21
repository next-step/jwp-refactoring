package kitchenpos.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.dto.order.TableGroupRequest;
import kitchenpos.dto.order.TableGroupResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceStep {

    private static final String API_URL = "/api/table-groups";

    public static ExtractableResponse<Response> 단체지정_등록_요청(TableGroupRequest tableGroupRequest) {
        return RestAssured
            .given().log().all()
            .body(tableGroupRequest)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(API_URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 단체지정_해지_요청(Long tableGroupId) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(API_URL + "/" + tableGroupId)
            .then().log().all()
            .extract();
    }

    public static Long 단체지정_등록_검증(ExtractableResponse<Response> response,
        TableGroupRequest expected) {
        TableGroupResponse 등록된_단체지정 = response.as(TableGroupResponse.class);

        assertAll(
            () -> assertThat(등록된_단체지정.getId()).isNotNull(),
            () -> assertThat(등록된_단체지정.getOrderTables()).extracting("id")
                .containsExactlyElementsOf(expected.getOrderTableIds())
        );
        
        return 등록된_단체지정.getId();
    }

    public static void 단체지정_해지_검증(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
