package kitchenpos.tablegroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.dto.OrderTableRequest;
import kitchenpos.order.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;

import static kitchenpos.ordertable.acceptance.TableAcceptanceTest.주문테이블_생성_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 관련 기능")
class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTableResponse 주문테이블_A;
    private OrderTableResponse 주문테이블_B;
    private TableGroupRequest 우테캠_PRO_단체;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블_A = 주문테이블_생성_요청(OrderTableRequest.of(4, true))
                .as(OrderTableResponse.class);
        주문테이블_B = 주문테이블_생성_요청(OrderTableRequest.of(4, true))
                .as(OrderTableResponse.class);
        우테캠_PRO_단체 = TableGroupRequest.of(Arrays.asList(주문테이블_A.getId(), 주문테이블_B.getId()));
    }

    @DisplayName("주문 테이블을 단체 지정한다.")
    @Test
    void createTableGroup() {
        // when
        ExtractableResponse<Response> response = 단체지정_생성_요청(우테캠_PRO_단체);

        // then
        단체지정_생성됨(response);
    }

    @DisplayName("단체 지정된 주문 테이블을 해제한다.")
    @Test
    void ungroup() {
        // given
        TableGroupResponse result = 단체지정_생성_요청(우테캠_PRO_단체).as(TableGroupResponse.class);

        // when
        ExtractableResponse<Response> response = 단체지정_해제_요청(result.getId());

        // then
        단체지정_해제됨(response);
    }

    private ExtractableResponse<Response> 단체지정_생성_요청(TableGroupRequest request) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
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
