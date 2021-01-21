package kitchenpos.tableGroup.acceptance;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.tableGroup.dto.TableGroupRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupAcceptanceTest extends AcceptanceTest {
    private List<Long> orderTableIds = new ArrayList<>();
    private TableGroupRequest tableGroupRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        orderTableIds.add(주문테이블_등록되어_있음(new OrderTableRequest( 4, true)).as(OrderTable.class).getId());
        orderTableIds.add(주문테이블_등록되어_있음(new OrderTableRequest( 10, true)).as(OrderTable.class).getId());
        tableGroupRequest = new TableGroupRequest(orderTableIds);

    }

    @DisplayName("테이블 그룹핑을 관리한다.")
    @Test
    void manageTableGrouping() {
        //등록
        ExtractableResponse<Response> createResponse = 테이블그룹핑_등록_요청(tableGroupRequest);
        테이블그룹핑_등록됨(createResponse);

        //해제
        ExtractableResponse<Response> removeResponse = 테이블그룹핑_해제_요청(createResponse);
        테이블그룹핑_해제됨(removeResponse);
    }

    private ExtractableResponse<Response> 주문테이블_등록되어_있음(OrderTableRequest orderTableRequest) {
        return RestAssured.given().log().all().
                body(orderTableRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/tables").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 테이블그룹핑_등록_요청(TableGroupRequest tableGroupRequest) {
        return RestAssured.given().log().all().
                body(tableGroupRequest).
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().post("/api/table-groups").
                then().log().all().
                extract();
    }

    private ExtractableResponse<Response> 테이블그룹핑_해제_요청(ExtractableResponse<Response> response) {
        String uri = response.header("Location");
        return RestAssured.given().log().all().
                contentType(MediaType.APPLICATION_JSON_VALUE).
                when().delete(uri).
                then().log().all().
                extract();
    }

    private void 테이블그룹핑_등록됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
        assertThat(response.as(TableGroupResponse.class).getOrderTables().get(0).getNumberOfGuests()).
                isEqualTo(4);
        assertThat(response.as(TableGroupResponse.class).getOrderTables().get(1).getNumberOfGuests()).
                isEqualTo(10);
        assertThat(response.as(TableGroupResponse.class).getCreatedDate()).isNotNull();
    }

    private void 테이블그룹핑_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }


}
