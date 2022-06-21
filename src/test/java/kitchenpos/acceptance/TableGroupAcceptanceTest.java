package kitchenpos.acceptance;

import static kitchenpos.acceptance.support.TableAcceptanceSupport.주문_테이블_등록요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertFalse;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("단체 지정 테이블에 대한 인수 테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTable 주문_테이블;
    private OrderTable 주문_테이블2;
    private TableGroup 테이블_그룹;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블 = 주문_테이블_등록요청(OrderTable.of(null, null, 3, true)).as(OrderTable.class);
        주문_테이블2 = 주문_테이블_등록요청(OrderTable.of(null, null, 1, true)).as(OrderTable.class);

        테이블_그룹 = TableGroup.of(null, null, Arrays.asList(주문_테이블, 주문_테이블2));
    }

    @DisplayName("테이블 단체지정을 등록한다")
    @Test
    void create_test() {
        // when
        ExtractableResponse<Response> response = 테이블_단체지정_등록요청(테이블_그룹);

        // then
        테이블_단체지정_등록됨(response);
    }

    @DisplayName("테이블 단체지정을 해제한다")
    @Test
    void ungroup_test() {
        // given
        TableGroup tableGroup = 테이블_단체지정_등록요청(테이블_그룹).as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = 테이블_단체지정_해제요청(tableGroup.getId());

        // then
        테이블_단체지정_해제됨(response);
    }

    private ExtractableResponse<Response> 테이블_단체지정_등록요청(TableGroup tableGroup) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .body(tableGroup)
            .when().post("/api/table-groups")
            .then().log().all()
            .extract();
    }

    private void 테이블_단체지정_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();

        TableGroup result = response.as(TableGroup.class);
        List<OrderTable> orderTables = result.getOrderTables();

        assertThat(result.getCreatedDate()).isNotNull();

        for (OrderTable orderTable : orderTables) {
            assertFalse(orderTable.isEmpty());
            assertThat(orderTable.getTableGroupId()).isNotNull();
        }
    }

    private ExtractableResponse<Response> 테이블_단체지정_해제요청(Long tableGroupId) {
        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
            .then().log().all()
            .extract();
    }

    private void 테이블_단체지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
