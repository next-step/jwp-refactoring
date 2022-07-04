package kitchenpos.tablegroup.acceptance;

import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import kitchenpos.common.acceptance.BaseAcceptanceTest;
import kitchenpos.fixture.acceptance.AcceptanceTestOrderTableFixture;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceTest extends BaseAcceptanceTest {
    private AcceptanceTestOrderTableFixture 주문_테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문_테이블 = new AcceptanceTestOrderTableFixture();
    }

    @DisplayName("테이블 단체 지정을 관리한다")
    @Test
    void manageTableGroup() {
        // when
        ExtractableResponse<Response> created = 테이블_단체_지정_요청(Arrays.asList(주문_테이블.빈_테이블1, 주문_테이블.빈_테이블2));
        // then
        테이블_단체_지정됨(created);

        // when
        ExtractableResponse<Response> list = 테이블_단체_해제_요청(created.as(TableGroupResponse.class).getId());
        // then
        테이블_단체_해제됨(list);
    }

    public static ExtractableResponse<Response> 테이블_단체_지정_요청(final List<OrderTableResponse> orderTables) {
        final Map<String, Object> body = new HashMap<>();
        body.put("orderTables", orderTables.stream().map(OrderTableResponse::getId).collect(Collectors.toList()));

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(body)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블_단체_해제_요청(final Long tableGroupId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
                .then().log().all()
                .extract();
    }

    public static void 테이블_단체_지정됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 테이블_단체_해제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
