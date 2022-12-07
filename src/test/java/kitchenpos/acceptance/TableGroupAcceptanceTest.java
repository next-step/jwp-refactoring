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

import java.time.LocalDateTime;
import java.util.Arrays;

import static kitchenpos.acceptance.TableAcceptanceTest.주문테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 관련 기능")
class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTable 주문테이블_A;
    private OrderTable 주문테이블_B;
    private TableGroup 우테캠_PRO_단체;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블_A = new OrderTable(1L, null, 4, true);
        주문테이블_B = new OrderTable(2L, null, 4, true);
        우테캠_PRO_단체 = new TableGroup(1L, null, Arrays.asList(주문테이블_A, 주문테이블_B));
    }

    @DisplayName("주문 테이블을 단체 지정한다.")
    @Test
    void createTableGroup() {
        // when
        주문테이블_A = 주문테이블_생성_요청(주문테이블_A).as(OrderTable.class);
        주문테이블_B = 주문테이블_생성_요청(주문테이블_B).as(OrderTable.class);
        우테캠_PRO_단체 = new TableGroup(1L, null, Arrays.asList(주문테이블_A, 주문테이블_B));
        ExtractableResponse<Response> response = 단체지정_생성_요청(우테캠_PRO_단체);

        // then
        단체지정_생성됨(response);
    }

    @DisplayName("단체 지정된 주문 테이블을 해제한다.")
    @Test
    void ungroup() {
        // given
        우테캠_PRO_단체 = 단체지정_생성_요청(우테캠_PRO_단체).as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = 단체지정_해제_요청(우테캠_PRO_단체.getId());

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
