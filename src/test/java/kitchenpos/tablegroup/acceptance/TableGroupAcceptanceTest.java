package kitchenpos.tablegroup.acceptance;

import static kitchenpos.ordertable.acceptance.TableAcceptanceTest.*;

import java.util.Arrays;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse orderTableResponse1;
    private OrderTableResponse orderTableResponse2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        orderTableResponse1 = 주문_테이블_생성_요청(new OrderTableRequest(2, true))
            .as(OrderTableResponse.class);
        orderTableResponse2 = 주문_테이블_생성_요청(new OrderTableRequest(2, true))
            .as(OrderTableResponse.class);
    }

    @DisplayName("단체 지정 테이블 그룹을 등록한다.")
    @Test
    void createTableGroup() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
            Arrays.asList(orderTableResponse1.getId(), orderTableResponse2.getId()));

        // when
        ExtractableResponse<Response> response = 테이블_그룹_생성_요청(tableGroupRequest);

        // then
        테이블_그룹_생성됨(response);
    }

    @DisplayName("단체 지정한 테이블을 제거한다.")
    @Test
    void ungroup() {
        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(
            Arrays.asList(orderTableResponse1.getId(), orderTableResponse2.getId()));
        TableGroupResponse tableGroupResponse = 테이블_그룹_생성_요청(tableGroupRequest).as(TableGroupResponse.class);

        // when
        ExtractableResponse<Response> response = 테이블_그룹의_테이블_제거_요청(tableGroupResponse.getId());

        // then
        테이블_그룹의_테이블_제거됨(response);
    }

    public static ExtractableResponse<Response> 테이블_그룹_생성_요청(TableGroupRequest request) {
        return RestAssured
            .given().log().all()
            .body(request)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .accept(MediaType.APPLICATION_JSON_VALUE)
            .when()
            .post("/api/table-groups")
            .then().log().all().extract();
    }

    private ExtractableResponse<Response> 테이블_그룹의_테이블_제거_요청(Long tableGroupId) {
        return RestAssured
            .given().log().all()
            .when()
            .delete("/api/table-groups/{tableGroupId}", tableGroupId)
            .then().log().all().extract();
    }

    private void 테이블_그룹_생성됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 테이블_그룹의_테이블_제거됨(ExtractableResponse<Response> response) {
        Assertions.assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }
}
