package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static kitchenpos.acceptance.TableAcceptanceTest.주문_테이블_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블 그룹 관련 기능")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/table-groups";

    private OrderTable 테이블1;
    private OrderTable 테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        테이블1 = 주문_테이블_등록되어_있음(6, true);
        테이블2 = 주문_테이블_등록되어_있음(4, true);
    }

    @DisplayName("테이블 그룹을 관리한다.")
    @Test
    void manageTableGroup() {
        // given
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(Arrays.asList(테이블1, 테이블2));

        // when
        ExtractableResponse<Response> 테이블_그룹_생성_응답 = 테이블_그룹_생성_요청(tableGroup);
        // then
        테이블_그룹_생성됨(테이블_그룹_생성_응답);

        // when
        Long 테이블_그룹_ID = 테이블_그룹_ID_조회(테이블_그룹_생성_응답);
        ExtractableResponse<Response> 테이블_그룹_해제_응답 = 테이블_그룹_해제_요청(테이블_그룹_ID);
        // then
        테이블_그룹_해제됨(테이블_그룹_해제_응답);
    }

    private ExtractableResponse<Response> 테이블_그룹_생성_요청(TableGroup params) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(params)
                .when().post(API_URL)
                .then().log().all()
                .extract();
    }

    private void 테이블_그룹_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private Long 테이블_그룹_ID_조회(ExtractableResponse<Response> response) {
        return response.jsonPath().getLong("id");
    }

    private ExtractableResponse<Response> 테이블_그룹_해제_요청(Long tableGroupId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete(String.format("%s/{tableGroupId}", API_URL), tableGroupId)
                .then().log().all()
                .extract();
    }

    private void 테이블_그룹_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
