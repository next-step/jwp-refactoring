package kitchenpos.tablegroup.ui;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.order.domain.Order;
import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.Collections;

import static kitchenpos.order.ui.OrderAcceptanceTest.주문_생성_요청;
import static kitchenpos.utils.TestHelper.*;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 관련 기능")
class TableGroupAcceptanceTest extends AcceptanceTest {
//    @DisplayName("주문 테이블들을 단체 지정으로 설정하거나 해제할 수 있다.")
//    @Test
//    void scenario() {
//        // given
//        OrderTable orderTable1 = OrderTable.of(빈_orderTable_id1, 0, true);
//        OrderTable orderTable2 = OrderTable.of(빈_orderTable_id2, 0, true);
//        TableGroup tableGroup = TableGroup.of(1L, Arrays.asList(orderTable1, orderTable2));
//
//        // when
//        ExtractableResponse<Response> response1 = 단체_지정_생성_요청(tableGroup);
//        // then
//        단체_지정됨(response1);
//
//        // when
//        ExtractableResponse<Response> response2 = 단체_지정_해제_요청(tableGroup.getId());
//        // then
//        단체_지정_해제됨(response2);
//    }
//
    @DisplayName("등록되어 있지 않은 주문 테이블이 있을 경우 생성되지 않는다.")
    @Test
    void createTableGroupException() {
        OrderTableRequest orderTable1 = new OrderTableRequest(빈_orderTable_id1, 0, true);
        OrderTableRequest orderTable2 = new OrderTableRequest(등록되어_있지_않은_orderTable_id, 0, true);
        TableGroupRequest tableGroupRequest = new TableGroupRequest(1L, Arrays.asList(orderTable1, orderTable2));

        ExtractableResponse<Response> response = 단체_지정_생성_요청(tableGroupRequest);

        단체_지정_실패(response);
    }
//
//    @DisplayName("들어간 주문들 중에서 주문 상태가 조리 또는 식사가 하나라도 있을 경우 해제할 수 없다.")
//    @Test
//    void ungroupTableGroupException() {
//        OrderTable orderTable1 = OrderTable.of(빈_orderTable_id1, 0, true);
//        OrderTable orderTable2 = OrderTable.of(빈_orderTable_id2, 0, true);
//        TableGroup tableGroup = TableGroup.of(1L, Arrays.asList(orderTable1, orderTable2));
//        Order order = Order.of(orderTable1.getId(), Collections.singletonList(OrderLineItem.of(1L, 등록된_menu_id, 2)));
//
//        단체_지정_생성_요청(tableGroup);
//        주문_생성_요청(order);
//        ExtractableResponse<Response> response = 단체_지정_해제_요청(tableGroup.getId());
//
//        단체_지정_해제_실패(response);
//    }

    private static ExtractableResponse<Response> 단체_지정_생성_요청(TableGroupRequest tableGroupRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    private static ExtractableResponse<Response> 단체_지정_해제_요청(Long tableGroupId) {
        return RestAssured
                .given().log().all()
                .when().delete("/api/table-groups/{tableGroupId}", tableGroupId)
                .then().log().all()
                .extract();
    }

    private static void 단체_지정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.header("Location")).isNotBlank();
    }

    private static void 단체_지정_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }

    private static void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private static void 단체_지정_해제_실패(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR.value());
    }
}
