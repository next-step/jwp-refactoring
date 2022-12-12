package kitchenpos.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_등록되어_있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable 주문_테이블_1;
    private OrderTable 주문_테이블_2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블_1 = 주문_테이블_등록되어_있음(2, true).as(OrderTable.class);
        주문_테이블_2 = 주문_테이블_등록되어_있음(4, true).as(OrderTable.class);
    }

    @Test
    @DisplayName("단체 지정을 등록할 수 있다.")
    void create() {
        // when
        ExtractableResponse<Response> response = 단체_지정_등록_요청(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // then
        단체_지정_등록됨(response);
    }

    @Test
    @DisplayName("단체 지정을 해제할 수 있다.")
    void ungroup() {
        // given
        TableGroup 단체 = 단체_지정_등록됨(Arrays.asList(주문_테이블_1, 주문_테이블_2)).as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = 단체_지정_해제_요청(단체.getId());

        // then
        단체_지정_해제됨(response);
    }


    public static ExtractableResponse<Response> 단체_지정_등록됨(List<OrderTable> orderTables) {
        return 단체_지정_등록_요청(orderTables);
    }

    public static ExtractableResponse<Response> 단체_지정_등록_요청(List<OrderTable> orderTables) {
        Map<String, Object> params = new HashMap<>();
        params.put("orderTables", orderTables);

        return RestAssured
                .given().log().all()
                .body(params)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static void 단체_지정_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static ExtractableResponse<Response> 단체_지정_해제_요청(Long tableGroupId) {
        return RestAssured
                .given().log().all()
                .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
                .then().log().all()
                .extract();
    }

    public static void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
