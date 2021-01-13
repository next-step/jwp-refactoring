package kitchenpos.tablegroup;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.OrderTableAcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse orderTable1;

    private OrderTableResponse orderTable2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        orderTable1 = OrderTableAcceptanceTest.주문_테이블_등록_되어있음(5, true).as(OrderTableResponse.class);
        orderTable2 = OrderTableAcceptanceTest.주문_테이블_등록_되어있음(3, true).as(OrderTableResponse.class);
    }

    @DisplayName("단체 지정을 관리한.")
    @Test
    void manage() {
        // given
        Map<String, Object> tableParma1 = new HashMap<>();
        tableParma1.put("id", orderTable1.getId());
        Map<String, Object> tableParma2 = new HashMap<>();
        tableParma2.put("id", orderTable2.getId());

        Map<String, Object> params = new HashMap<>();
        params.put("orderTables", Arrays.asList(tableParma1, tableParma2));

        // when
        ExtractableResponse<Response> createResponse = 단체_지정_생성_요청(params);

        // then
        단체_지정_생성됨(createResponse);

        // when
        ExtractableResponse<Response> deleteResponse = 단체_지정_삭제_요청(createResponse);

        // then
        단체_지정_삭제됨(deleteResponse);
    }

    private ExtractableResponse<Response> 단체_지정_생성_요청(final Map<String, Object> params) {
        return RestAssured
                .given().log().all().body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 단체_지정_삭제_요청(final ExtractableResponse<Response> createResponse) {
        String location = createResponse.header("Location");
        return RestAssured
                .given().log().all()
                .when().delete(location)
                .then().log().all()
                .extract();
    }

    private void 단체_지정_생성됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 단체_지정_삭제됨(final ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
