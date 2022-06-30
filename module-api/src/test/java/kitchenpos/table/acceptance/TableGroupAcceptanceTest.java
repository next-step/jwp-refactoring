package kitchenpos.table.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 관련 기능")
class TableGroupAcceptanceTest extends AcceptanceTest {

    TableGroupRequest tableGroupRequest;

    @BeforeEach
    public void init() {
        super.init();

        // given
        TableResponse 테이블1 = TableAcceptanceTest.주문_테이블_생성되어_있음(0, true).as(TableResponse.class);
        TableResponse 테이블2 = TableAcceptanceTest.주문_테이블_생성되어_있음(0, true).as(TableResponse.class);
        tableGroupRequest = new TableGroupRequest(Arrays.asList(테이블1.getId(), 테이블2.getId()));
    }

    @DisplayName("주문 테이블들 단체 지정에 성공한다.")
    @Test
    void 단체_지정() {
        // when
        ExtractableResponse<Response> response = 주문_테이블들_단체_지정_요청(tableGroupRequest);

        // then
        단체_지정됨(response);
    }

    @DisplayName("주문 테이블 단체 지정 해제에 성공한다.")
    @Test
    void 단체_지정_해제() {
        // given
        ExtractableResponse<Response> deleteResponse = 주문_테이블들_단체_지정_요청(tableGroupRequest);

        // when
        ExtractableResponse<Response> response = 단체_지정_해제_요청(deleteResponse);

        // then
        단체_지정_해제됨(response);
    }

    public static ExtractableResponse<Response> 주문_테이블들_단체_지정되어_있음(ExtractableResponse<Response>... responses) {
        List<Long> tableIds = Arrays.stream(responses)
                .map(response -> response.as(TableResponse.class))
                .map(table -> table.getId())
                .collect(Collectors.toList());
        return 주문_테이블들_단체_지정_요청(new TableGroupRequest(tableIds));
    }

    public static ExtractableResponse<Response> 주문_테이블들_단체_지정_요청(TableGroupRequest tableGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 단체_지정_해제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");

        return RestAssured
                .given().log().all()
                .when().delete(uri)
                .then().log().all()
                .extract();
    }

    public static void 단체_지정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
