package kitchenpos.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.acceptance.TableAcceptanceTest.주문_테이블_생성_요청;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DisplayName("단체지정 관련 기능 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;
    private TableGroup 단체;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블1 = 주문_테이블_생성_요청(new OrderTable(1L, 4, true)).as(OrderTable.class);
        주문테이블2 = 주문_테이블_생성_요청(new OrderTable(2L, 4, true)).as(OrderTable.class);
        단체 = new TableGroup(1L, LocalDateTime.now());
        단체.group(Arrays.asList(주문테이블1, 주문테이블2));
    }

    @DisplayName("주문 테이블의 단체를 지정한다.")
    @Test
    void createTableGroup() {
        // when
        ExtractableResponse<Response> response = 단체_지정_요청(단체);

        // then
        단체_지정됨(response);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        단체 = 단체_지정_요청(단체).as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = 단체_지정_해제_요청(단체.getId());

        // then
        단체_지정_해제됨(response);
    }

    private static void 단체_지정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }

    public static ExtractableResponse<Response> 단체_지정_요청(TableGroup tableGroup) {
        return RestAssured
                .given().log().all()
                .body(tableGroup)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 단체_지정_해제_요청(Long tableGroupId) {
        return RestAssured
                .given().log().all()
                .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
                .then().log().all()
                .extract();
    }

}
