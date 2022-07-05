package kitchenpos.table.acceptance;

import static kitchenpos.table.acceptance.TableAcceptanceTest.주문_테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("테이블 그룹을 관리한다.")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTableResponse 일번_주문_테이블;
    private OrderTableResponse 이번_주문_테이블;

    @BeforeEach
    void init() {
        // given
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

    @Test
    @DisplayName("주문 테이블의 단체 지정을 해제한다.")
    void ungroup() {
        // given
        TableGroupResponse 테이블_그룹 = 테이블_그룹_생성_요청(Arrays.asList(일번_주문_테이블.getId(), 이번_주문_테이블.getId())).as(TableGroupResponse.class);

        // when
        ExtractableResponse<Response> response = 테이블_그룹_해제_요청(테이블_그룹.getId());

        // then
        테이블_그룹_삭제됨(response);
    }

    public static ExtractableResponse<Response> 테이블_그룹_생성_요청(List<Long> orderTables) {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTables);

        return RestAssured.given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tableGroupRequest)
            .when().post("/api/table-groups")
            .then().log().all().extract();
    }

    public static ExtractableResponse<Response> 테이블_그룹_해제_요청(Long tableGroupId) {
        return RestAssured.given().log().all()
            .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
            .then().log().all().extract();
    }

    public static void 테이블_그룹_생성됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 테이블_그룹_삭제됨(ExtractableResponse response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
