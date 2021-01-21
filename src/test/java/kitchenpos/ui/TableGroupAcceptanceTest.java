package kitchenpos.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import kitchenpos.order.acceptance.OrderTableAcceptanceTest;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private List<OrderTable> orderTables = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        super.setUp();
        ExtractableResponse<Response> listResponse = OrderTableAcceptanceTest.주문_테이블_목록_조회_요청();
        orderTables = listResponse.jsonPath().getList(".", OrderTable.class);
    }

    @DisplayName("단체 지정")
    @Test
    void create() {
        // when
        Map<String, Object> params = new HashMap<>();
        params.put("orderTables", orderTables);
        ExtractableResponse<Response> response = 주문_단체_등록_요청(params);

        // then
        주문_단체_둥록됨(response);
    }

    @DisplayName("단체 지정 해제")
    @Test
    void ungroup() {
        // given
        Map<String, Object> params = new HashMap<>();
        params.put("orderTables", orderTables);
        ExtractableResponse<Response> createdResponse = 주문_단체_등록_요청(params);
        TableGroup tableGroup = createdResponse.as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = 주문_단체_해제_요청(tableGroup.getId());

        // then
        주문_단체_해제됨(response, HttpStatus.NO_CONTENT);
    }

    private ExtractableResponse<Response> 주문_단체_등록_요청(Map<String, Object> params) {
        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 주문_단체_해제_요청(Long id) {
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/api/table-groups/{id}", id)
                .then().log().all().extract();
        return response;
    }

    private void 주문_단체_해제됨(ExtractableResponse<Response> response, HttpStatus ok) {
        Assertions.assertThat(response.statusCode()).isEqualTo(ok.value());
    }



    private void 주문_단체_둥록됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }
}
