package kitchenpos.table.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.dto.MenuResponse;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.menu.acceptance.MenuAcceptanceTest.메뉴_생성을_요청;
import static kitchenpos.menu.acceptance.MenuGroupAcceptanceTest.메뉴그룹_생성을_요청;
import static kitchenpos.order.acceptance.OrderAcceptanceTest.주문_생성을_요청;
import static kitchenpos.table.acceptance.TableAcceptanceTest.테이블_생성을_요청;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    @DisplayName("등록되어 있지 않은 테이블이있으면 그룹생성에 실패")
    @Test
    void createTableGroupWithNullOrderTable() {
        ExtractableResponse<Response> response = 테이블그룹_생성을_요청(Arrays.asList(-1L, -2L));

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("테이블목록이 없으면 그룹생성에 실패")
    @Test
    void createTableGroupWithEmptyOrderTables() {
        ExtractableResponse<Response> response = 테이블그룹_생성을_요청(Collections.EMPTY_LIST);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("2보다작은 테이블목록이있으면 그룹생성에 실패")
    @Test
    void createTableGroupWithOrderTablesLessThenTwo() {
        OrderTableResponse 주문_테이블 = 테이블_생성을_요청(1, true).as(OrderTableResponse.class);
        ExtractableResponse<Response> response = 테이블그룹_생성을_요청(Arrays.asList(주문_테이블.getId()));

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("공석이 아닌 테이블이포함되어있으면 그룹생성에 실패")
    @Test
    void createTableWithNotEmptyOrderTable() {
        OrderTableResponse 주문_테이블1 = 테이블_생성을_요청(1, true).as(OrderTableResponse.class);
        OrderTableResponse 주문_테이블2 = 테이블_생성을_요청(1, false).as(OrderTableResponse.class);

        ExtractableResponse<Response> response = 테이블그룹_생성을_요청(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId()));

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("테이블그룹을 생성한다")
    @Test
    void createTable() {
        OrderTableResponse 주문_테이블1 = 테이블_생성을_요청(1, true).as(OrderTableResponse.class);
        OrderTableResponse 주문_테이블2 = 테이블_생성을_요청(2, true).as(OrderTableResponse.class);

        ExtractableResponse<Response> response = 테이블그룹_생성을_요청(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId()));

        assertEquals(HttpStatus.CREATED.value(), response.statusCode());
    }

    @DisplayName("등록되지 않은 테이블그룹을 해제하면 실패")
    @Test
    void ungroupWithNullTableGroup() {
        ExtractableResponse<Response> response = 테이블그룹_해제를_요청(-1L);

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    @DisplayName("계산전 테이블을의 테이블 그룹을 해제하면 실패")
    @Test
    void ungroupWithCookingOrEatingOrder() {
        OrderTableResponse 주문_테이블1 = 테이블_생성을_요청(1, true).as(OrderTableResponse.class);
        OrderTableResponse 주문_테이블2 = 테이블_생성을_요청(1, true).as(OrderTableResponse.class);

        MenuGroupResponse 치킨 = 메뉴그룹_생성을_요청("치킨").as(MenuGroupResponse.class);
        MenuResponse 양념치킨 = 메뉴_생성을_요청("양념치킨", new BigDecimal(0), 치킨.getId(),
                Collections.emptyList()).as(MenuResponse.class);
        TableGroupResponse 테이블그룹 = 테이블그룹_생성을_요청(Arrays.asList(주문_테이블1.getId(), 주문_테이블2.getId())).as(TableGroupResponse.class);
        주문_생성을_요청(주문_테이블1.getId(), Arrays.asList(new OrderLineItemRequest(양념치킨.getId(), 1L)))
                .as(OrderResponse.class);
        주문_생성을_요청(주문_테이블2.getId(), Arrays.asList(new OrderLineItemRequest(양념치킨.getId(), 1L)))
                .as(OrderResponse.class);

        ExtractableResponse<Response> response = 테이블그룹_해제를_요청(테이블그룹.getId());

        assertEquals(HttpStatus.BAD_REQUEST.value(), response.statusCode());
    }

    public static ExtractableResponse<Response> 테이블그룹_생성을_요청(List<Long> orderTables) {
        TableGroupRequest request = new TableGroupRequest(orderTables);
        return RestAssured.given().log().all()
                .body(request)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static ExtractableResponse<Response> 테이블그룹_해제를_요청(Long tableGroupId) {
        return RestAssured.given().log().all()
                .when().delete("/api/table-groups/" + tableGroupId)
                .then().log().all()
                .extract();
    }
}