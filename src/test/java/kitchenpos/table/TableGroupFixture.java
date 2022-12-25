package kitchenpos.table;

import static kitchenpos.table.TableFixture.빈테이블;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import kitchenpos.order.domain.OrderTable;
import kitchenpos.table.domain.TableGroup;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import org.springframework.http.MediaType;

public class TableGroupFixture {

    public static TableGroup 단체지정 = new TableGroup(1L, Arrays.asList(빈테이블, 빈테이블));

    public static ExtractableResponse<Response> 단체_지정(List<Long> orderTableIds) {
        TableGroupRequest tableGroup = new TableGroupRequest(orderTableIds);

        return RestAssured
            .given().log().all()
            .body(tableGroup)
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().post("/api/table-groups")
            .then().log().all()
            .extract();
    }

    public static ExtractableResponse<Response> 단체_지정_해제(long tableGroupId) {

        return RestAssured
            .given().log().all()
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .when().delete("/api/table-groups/" + tableGroupId)
            .then().log().all()
            .extract();
    }

    public static TableGroupRequest createTableGroupRequest(TableGroup tableGroup) {
        return new TableGroupRequest(tableGroup.getId(), tableGroup.getCreatedDate(),
            createOrderTableRequests(tableGroup.getOrderTables()));
    }

    public static List<OrderTableRequest> createOrderTableRequests(List<OrderTable> orderTables) {
        if (Objects.isNull(orderTables)) {
            return Collections.emptyList();
        }
        return orderTables.stream()
            .map(orderTable -> new OrderTableRequest(orderTable.getId(),
                orderTable.getTableGroupId(), orderTable.getNumberOfGuests(), orderTable.isEmpty()))
            .collect(Collectors.toList());
    }
}
