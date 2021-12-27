package kitchenpos.table;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.table.TableAcceptanceTest.주문_테이블_등록_요청;
import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("단체 지정 관련 기능")
public class TableGroupAcceptanceTest extends AcceptanceTest {
    @Test
    @DisplayName("단체 지정 관리 기능")
    void tableGroup() {
        OrderTable 주문테이블1 = 주문_테이블_등록_요청(true, 4).as(OrderTable.class);
        OrderTable 주문테이블2 = 주문_테이블_등록_요청(true, 4).as(OrderTable.class);
        // when
        ExtractableResponse<Response> response = 단체_지정(Arrays.asList(new OrderTableRequest(주문테이블1.getId()), new OrderTableRequest(주문테이블2.getId())));

        // then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(TableGroupResponse.class).getOrderTables()).hasSize(2);
        assertThat(response.as(TableGroupResponse.class).getOrderTables().get(0).getTableGroupId()).isNotNull();

        // when
        ExtractableResponse<Response> deleteResponse = 단체_지정_해제(response.as(TableGroupResponse.class));

        // then
        assertThat(deleteResponse.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    private ExtractableResponse<Response> 단체_지정_해제(TableGroupResponse tableGroup) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{id}", tableGroup.getId())
                .then().log().all().extract();
    }

    private ExtractableResponse<Response> 단체_지정(List<OrderTableRequest> orderTableRequests) {
        TableGroupRequest tableGroupRequest = new TableGroupRequest(orderTableRequests);
        return RestAssured
                .given().log().all()
                .body(tableGroupRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups")
                .then().log().all().extract();
    }
}
