package kitchenpos.acceptance.step;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.TableGroup;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceStep {

    private static final String TABLE_GROUP_API_URL = "/api/table-groups";

    public static ExtractableResponse<Response> 단체지정_등록_요청(TableGroup 등록_단체지정) {
        return RestAssured
            .given().log().all()
            .body(등록_단체지정)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post(TABLE_GROUP_API_URL)
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 단체지정_해지_요청(Long 단체지정번호) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete(TABLE_GROUP_API_URL + "/" + 단체지정번호)
            .then().log().all()
            .extract();
    }

    public static TableGroup 단체지정_등록_검증(ExtractableResponse<Response> 단체지정_등록_결과,
        TableGroup 예상_단체지정) {
        TableGroup 등록된_단체지정 = 단체지정_등록_결과.as(TableGroup.class);
        assertThat(등록된_단체지정.getId()).isNotNull();
        assertThat(등록된_단체지정.getOrderTables()).containsExactlyElementsOf(예상_단체지정.getOrderTables());

        return 등록된_단체지정;
    }

    public static void 단체지정_해지_검증(ExtractableResponse<Response> 단체지정_해지_결과) {
        assertThat(단체지정_해지_결과.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
