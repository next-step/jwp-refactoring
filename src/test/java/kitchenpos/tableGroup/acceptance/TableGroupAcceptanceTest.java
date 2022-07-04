package kitchenpos.tableGroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.orderTable.dto.OrderTableResponse;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.orderTable.acceptance.OrderTableAcceptanceTest.주문_테이블_생성되어_있음;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("단체 지정을 생성한다.")
    @Test
    void create() {
        // given
        OrderTableResponse orderTableResponse1 = 주문_테이블_생성되어_있음(0, true).as(OrderTableResponse.class);
        OrderTableResponse orderTableResponse2 = 주문_테이블_생성되어_있음(0, true).as(OrderTableResponse.class);
        List<Long> orderTableIds = Arrays.asList(orderTableResponse1.getOrderTableId(), orderTableResponse2.getOrderTableId());

        // when
        ExtractableResponse<Response> response = 단체_지정_요청(orderTableIds);

        // then
        단체_지정_요청_됨(response, orderTableResponse1.getOrderTableId(), orderTableResponse2.getOrderTableId());
    }

    @DisplayName("주문 테이블이 2개 이상이여야 한다.")
    @Test
    void create1() {
        // given
        OrderTableResponse orderTableResponse = 주문_테이블_생성되어_있음(0, true).as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = 단체_지정_요청(Collections.singletonList(orderTableResponse.getOrderTableId()));

        // then
        단체_지정_요청_실패됨(response);
    }

    @DisplayName("단체 지정한 주문 테이블 수와 조회한(저장된) 주문 테이블 수가 일치해야한다.")
    @Test
    void create2() {
        // given
        OrderTableResponse orderTableResponse = 주문_테이블_생성되어_있음(0, true).as(OrderTableResponse.class);

        // when
        ExtractableResponse<Response> response = 단체_지정_요청(Arrays.asList(orderTableResponse.getOrderTableId(), 2L));

        // then
        단체_지정_요청_실패됨(response);
    }

    @DisplayName("저장 요청한 주문 테이블은 단체 지정되어 있으면 안된다.")
    @Test
    void create3() {
        // given
        OrderTableResponse orderTableResponse = 주문_테이블_생성되어_있음(0, true).as(OrderTableResponse.class);

        // when
        단체_지정_요청(Arrays.asList(orderTableResponse.getOrderTableId(), 2L));
        ExtractableResponse<Response> response = 단체_지정_요청(Arrays.asList(orderTableResponse.getOrderTableId(), 2L));

        // then
        단체_지정_요청_실패됨(response);
    }

    private static ExtractableResponse<Response> 단체_지정_요청(List<Long> orderTableIds) {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableIds);

        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    private static void 단체_지정_요청_됨(ExtractableResponse<Response> response, Long... orderTableIds) {
        TableGroupResponse tableGroupResponse = response.as(TableGroupResponse.class);

        assertAll(
                () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
                () -> assertThat(tableGroupResponse.getTableGroupId()).isNotNull(),
                () -> assertThat(tableGroupResponse.getOrderTableIds()).containsExactly(orderTableIds)
        );
    }

    private void 단체_지정_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.BAD_REQUEST.value());
    }
}
