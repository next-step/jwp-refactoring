package kitchenpos.acceptance.step;

import static kitchenpos.acceptance.step.TableAcceptanceStep.테이블_목록_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.tuple;
import static org.junit.jupiter.api.Assertions.assertAll;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.mapper.TypeRef;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;
import org.assertj.core.groups.Tuple;
import org.springframework.http.HttpStatus;

public class TableGroupAcceptanceStep {

    public static TableGroup 단체_지정_되어_있음(List<Long> orderTableIds) {
        return 단체_지정_요청(orderTableIds).as(TableGroup.class);
    }

    public static ExtractableResponse<Response> 단체_지정_요청(List<Long> orderTableIds) {
        return RestAssured.given().log().all()
            .contentType(ContentType.JSON)
            .body(tableGroupRequest(orderTableIds))
            .when()
            .post("/api/table-groups")
            .then().log().all()
            .extract();
    }

    public static void 단체_지정_됨(ExtractableResponse<Response> response,
        List<Long> expectedOrderTableIds) {
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value()),
            () -> assertThat(response.as(TableGroup.class))
                .satisfies(group -> {
                    assertThat(group.getId()).isNotNull();
                    assertThat(group.getCreatedDate())
                        .isEqualToIgnoringMinutes(LocalDateTime.now());
                    assertThat(group.getOrderTables())
                        .extracting(OrderTable::getId, OrderTable::getTableGroupId)
                        .containsExactly(
                            expectedOrderTableIds
                                .stream()
                                .map(id -> tuple(id, group.getId()))
                                .toArray(Tuple[]::new)
                        );
                })
        );
    }

    public static ExtractableResponse<Response> 단체_해제_요청(TableGroup tableGroup) {
        return RestAssured.given().log().all()
            .when()
            .delete("/api/table-groups/{tableGroupId}", tableGroup.getId())
            .then().log().all()
            .extract();
    }

    public static void 단체_해제_됨(ExtractableResponse<Response> response) {
        List<OrderTable> orderTableResponse = 테이블_목록_조회_요청().as(new TypeRef<List<OrderTable>>() {
        });
        assertAll(
            () -> assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value()),
            () -> assertThat(orderTableResponse)
                .extracting(OrderTable::getTableGroupId)
                .containsExactly(null, null)
        );
    }

    private static TableGroup tableGroupRequest(List<Long> orderTableIds) {
        TableGroup tableGroup = new TableGroup();
        tableGroup.setOrderTables(
            orderTableIds.stream()
                .map(TableGroupAcceptanceStep::orderTableRequest)
                .collect(Collectors.toList())
        );
        return tableGroup;
    }

    private static OrderTable orderTableRequest(Long orderTableId) {
        OrderTable orderTable = new OrderTable();
        orderTable.setId(orderTableId);
        return orderTable;
    }
}
