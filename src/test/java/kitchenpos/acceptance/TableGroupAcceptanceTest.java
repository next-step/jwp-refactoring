package kitchenpos.acceptance;

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

import static kitchenpos.acceptance.TableAcceptanceTest.주문테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 관련 인수 테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTable 주문테이블_A;
    private OrderTable 주문테이블_B;
    private TableGroup 개발자_모임_단체;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블_A = 주문테이블_생성_요청(new OrderTable(null, null, 4, true))
                .as(OrderTable.class);
        주문테이블_B = 주문테이블_생성_요청(new OrderTable(null, null, 4, true))
                .as(OrderTable.class);
        개발자_모임_단체 = new TableGroup(1L, null, Arrays.asList(주문테이블_A, 주문테이블_B));
    }

    @Test
    void 주문_테이블을_단체_지정한다() {
        // when
        ExtractableResponse<Response> response = 단체지정_생성_요청(개발자_모임_단체);

        // then
        단체지정_생성됨(response);
    }

    @Test
    void 단체_지정된_주문_테이블을_해제한다() {
        // given
        개발자_모임_단체 = 단체지정_생성_요청(개발자_모임_단체).as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = 단체지정_해제_요청(개발자_모임_단체.getId());

        // then
        단체지정_해제됨(response);
    }

    private ExtractableResponse<Response> 단체지정_생성_요청(TableGroup tableGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroup)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 단체지정_해제_요청(Long id) {
        return RestAssured
                .given().log().all()
                .when().delete("/api/table-groups/{id}", id)
                .then().log().all()
                .extract();
    }

    private void 단체지정_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 단체지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }
}
