package kitchenpos.acceptance;

import static kitchenpos.acceptance.TableAcceptanceTest.주문테이블_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import kitchenpos.AcceptanceTest;
import kitchenpos.fixture.TableGroupFactory;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    private OrderTableResponse 주문테이블1;
    private OrderTableResponse 주문테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블1 = 주문테이블_등록_요청(5, true).as(OrderTableResponse.class);
        주문테이블2 = 주문테이블_등록_요청(5, true).as(OrderTableResponse.class);
    }

    /**
     * Feature: 테이블 목록을 묶어 단체로 관리 할 수 있다.
     * <p>
     * Scenario: 단체 지정 관리
     * <p>
     * When: 테이블 목록을 묶어 단체 지정 요청
     * <p>
     * Then: 단체 지정 등록됨
     * <p>
     * When: 단체 지정 삭제 요청
     * <p>
     * Then: 단체 지정 삭제됨
     */
    @Test
    void 단체지정_관리() {
        ExtractableResponse<Response> response;
        // when 단체 지정 요청
        response = 단체_지정_요청(Arrays.asList(주문테이블1.getId(), 주문테이블2.getId()));
        // then
        단체_지정_등록됨(response);

        // when 단체 지정 삭제 요청
        response = 단체_지정_삭제_요청(response.as(TableGroupResponse.class));
        // then
        단체_지정_삭제됨(response);
    }

    public static ExtractableResponse<Response> 단체_지정_요청(List<Long> orderTableIds) {
        TableGroupRequest request = TableGroupFactory.createTableGroupRequest(orderTableIds);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(request)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 단체_지정_삭제_요청(TableGroupResponse tableGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{id}", tableGroup.getId())
                .then().log().all()
                .extract();
    }

    public static void 단체_지정_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 단체_지정_삭제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
