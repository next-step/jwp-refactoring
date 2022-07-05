package kitchenpos.table.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

@DisplayName("테이블 그룹을 관리한다.")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTableResponse 일번_주문_테이블;
    private OrderTableResponse 이번_주문_테이블;

    @BeforeEach
    void init() {
        일번_주문_테이블 = 주문_테이블_생성_요청(4, true).as(OrderTableResponse.class);
        이번_주문_테이블 = 주문_테이블_생성_요청(7, true).as(OrderTableResponse.class);
    }

    @Test
    @DisplayName("테이블 그룹을 생성한다.")
    void createTable() {
        // when
        ExtractableResponse<Response> response = 테이블_그룹_생성_요청(Arrays.asList(일번_주문_테이블.getId(), 이번_주문_테이블.getId()));

        // then
        테이블_그룹_생성됨(response);
    }

    public static ExtractableResponse<Response> 테이블_그룹_생성_요청(List<Long> orderTables) {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTables);

        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(tableGroupRequest)
            .when().post("/api/table-groups")
            .then().log().all().extract();
    }

    public static void 테이블_그룹_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }
}
