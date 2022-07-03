package kitchenpos.application.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.application.acceptance.OrderTableAcceptanceTest.주문_테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;


@DisplayName("단체 지정 관련 기능")
class TableGroupAcceptanceTest extends BaseAcceptanceTest {

    @BeforeEach
    public void setUp() {
        super.setUp();
    }

    /**
     * Given 주문 테이블을 2건 생성 후
     * When 단체 지정을 생성하면
     * Then 단체가 생성된다.
     */
    @DisplayName("단체 지정 생성")
    @Test
    void createTableGroup() {
        // given
        OrderTable 주문_테이블_1 = 주문_테이블_생성_요청(3, true).as(OrderTable.class);
        OrderTable 주문_테이블_2 = 주문_테이블_생성_요청(2, true).as(OrderTable.class);

        // when
        ExtractableResponse<Response> 단체_지정_생성_요청_응답 = 단체_지정_생성_요청(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // then
        assertAll(
                () -> assertEquals(HttpStatus.CREATED.value(), 단체_지정_생성_요청_응답.statusCode()),
                () -> assertNotNull(단체_지정_생성_요청_응답.jsonPath().get("id")),
                () -> assertThat(단체_지정_생성_요청_응답.jsonPath().getList("orderTables")).hasSize(2)
        );
    }

    /**
     * Given 주문 테이블을 1건 생성 후
     * When 단체 지정을 생성하면
     * Then 오류가 발생한다.
     */
    @DisplayName("주문 테이블을 1건 생성 후 단체 지정 생성")
    @Test
    void createTableGroupWithOneOrderTable() {
        // given
        OrderTable 주문_테이블_1 = 주문_테이블_생성_요청(3, true).as(OrderTable.class);

        // when
        ExtractableResponse<Response> 단체_지정_생성_요청_응답 = 단체_지정_생성_요청(Arrays.asList(주문_테이블_1));

        // then
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR.value(), 단체_지정_생성_요청_응답.statusCode());
    }

    /**
     * Given 주문 테이블을 2건 생성 후
     * When 단체 지정을 생성하면
     * Then 생성된 데이터 내 주문 테이블의 빈 테이블 여부가 false 로 지정된다.
     */
    @DisplayName("저장된 데이터와 요청 데이터의 주문 테이블의 개수가 다른 경우")
    @Test
    void createTableGroupWithEmptyFalse() {
        // given
        OrderTable 주문_테이블_1 = 주문_테이블_생성_요청(3, true).as(OrderTable.class);
        OrderTable 주문_테이블_2 = 주문_테이블_생성_요청(2, true).as(OrderTable.class);

        // when
        ExtractableResponse<Response> 단체_지정_생성_요청_응답 = 단체_지정_생성_요청(Arrays.asList(주문_테이블_1, 주문_테이블_2));

        // then
        assertFalse(단체_지정_생성_요청_응답.jsonPath().getBoolean("orderTables.empty"));
    }

    public static ExtractableResponse<Response> 단체_지정_생성_요청(List<OrderTable> orderTables) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(orderTables);

        return RestAssured
                .given().log().all()
                .body(tableGroup)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 단체_지정_해제_요청(long tableGroupId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
                .then().log().all()
                .extract();
    }
}
