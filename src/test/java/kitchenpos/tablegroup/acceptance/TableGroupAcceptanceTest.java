package kitchenpos.tablegroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.OrderTableIdRequest;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private static final String API_URL = "/api/table-groups";

    private OrderTableResponse 주문_테이블_네명;
    private OrderTableResponse 주문_테이블_여섯명;

    private static ExtractableResponse<Response> 테이블_그룹_생성_요청(List<OrderTableIdRequest> orderTables) {
        TableGroupRequest tableGroupRequest = TableGroupRequest.of(orderTables);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().post(API_URL)
                .then().log().all()
                .extract();
    }

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블_네명 = 주문_테이블_등록되어_있음(6, true);
        주문_테이블_여섯명 = 주문_테이블_등록되어_있음(4, true);
    }

    @Test
    @DisplayName("테이블 그룹 생성 및 해제 시나리오")
    void tableGroup() {
        ExtractableResponse<Response> 테이블_그룹_생성_요청_결과 =
                테이블_그룹_생성_요청(Arrays.asList(
                        OrderTableIdRequest.of(주문_테이블_네명.getId()),
                        OrderTableIdRequest.of(주문_테이블_여섯명.getId()))
                );

        테이블_그룹_생성됨(테이블_그룹_생성_요청_결과);

        Long 테이블_그룹_ID = 테이블_그룹_ID_조회(테이블_그룹_생성_요청_결과);

        ExtractableResponse<Response> 테이블_그룹_해제_요청_결과 = 테이블_그룹_해제_요청(테이블_그룹_ID);

        테이블_그룹_해제됨(테이블_그룹_해제_요청_결과);
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
