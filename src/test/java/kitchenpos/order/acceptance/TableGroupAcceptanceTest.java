package kitchenpos.order.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import static kitchenpos.order.acceptance.OrderTableAcceptanceTest.주문테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("테이블그룹(단체) 관련 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTable 주문테이블1;
    private OrderTable 주문테이블2;

    @Override
    @BeforeEach
    public void setUp() {
        super.setUp();

        주문테이블1 = 주문테이블_생성_요청(4, true).as(OrderTable.class);
        주문테이블2 = 주문테이블_생성_요청(6, true).as(OrderTable.class);
    }

    /**
     * Given : 단체로 지정할 주문테이블이 2개 이상 존재한다.
     * When : 단체테이블로 지정 요청한다.
     * Then : 단체테이블로 그룹화가 된다.
     */
    @DisplayName("단체 테이블 생성 요청 인수 테스트")
    @Test
    void createTableGroup() {
        // when
        ExtractableResponse<Response> response = 단체테이블_생성_요청(주문테이블1, 주문테이블2);

        // then
        단체테이블_생성됨(response);
    }

    /**
     * Given : 주문 테이블 2개가 존재하고, 그 중 1개는 빈 테이블로 존재한다.
     * When : 단체테이블로 지정 요청한다.
     * Then : 단체테이블로 그룹화가 실패한다.
     */
    @DisplayName("단체 테이블 생성 요청 인수 테스트")
    @Test
    void createTableGroupExceptionNotEmptyTable() {
        // given
        OrderTable 주문테이블3 = 주문테이블_생성_요청(4, false).as(OrderTable.class);

        // when
        ExtractableResponse<Response> response = 단체테이블_생성_요청(주문테이블1, 주문테이블3);

        // then
        단체테이블_생성_실패됨(response);
    }

    /**
     * Given : 단체테이블로 지정된 테이블이 존재한다.
     * When : 단체테이블을 각각의 주문테이블로 분리한다.
     * Then : 단체 테이블이 해체된다.
     */
    @DisplayName("단체테이블 해체 요청 인수테스트")
    @Test
    void ungroupTables() {
        // given
        TableGroup tableGroup = 단체테이블_생성_요청(주문테이블1, 주문테이블2).as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = RestAssured
                .given().log().all()
                .when().delete("/api/table-groups/{tableGroupId}", tableGroup.getId())
                .then().log().all()
                .extract();

        // then
        단체테이블_해체됨(response);
    }

    public static ExtractableResponse<Response> 단체테이블_생성_요청(OrderTable mergeSource1, OrderTable mergeSource2) {
        TableGroup tableGroupRequest = new TableGroup(Lists.newArrayList(mergeSource1, mergeSource2));
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static void 단체테이블_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    public static void 단체테이블_해체됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 단체테이블_생성_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
