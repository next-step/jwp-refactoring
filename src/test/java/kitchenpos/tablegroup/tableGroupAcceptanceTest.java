package kitchenpos.tablegroup;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static kitchenpos.table.tableAcceptanceTest.주문_테이블_등록_되어있음;
import static kitchenpos.table.tableAcceptanceTest.주문_테이블_조회_요청;
import static org.assertj.core.api.Assertions.assertThat;

public class tableGroupAcceptanceTest extends AcceptanceTest {
    private TableGroupRequest tableGroupRequest;

    @BeforeEach
    public void setUp() {
        super.setUp();
        tableGroupRequest = new TableGroupRequest(generateOrderTableRequests());
    }

    private List<OrderTableRequest> generateOrderTableRequests() {
        OrderTableResponse orderTableResponse1 = 주문_테이블_등록_되어있음(new OrderTableRequest( 1, true));
        OrderTableResponse orderTableResponse2 = 주문_테이블_등록_되어있음(new OrderTableRequest( 2, true));
        OrderTableRequest orderTableRequest1 = new OrderTableRequest(orderTableResponse1.getId(), orderTableResponse1.getNumberOfGuests(), orderTableResponse1.isEmpty());
        OrderTableRequest orderTableRequest2 = new OrderTableRequest(orderTableResponse2.getId(), orderTableResponse2.getNumberOfGuests(), orderTableResponse2.isEmpty());
        return Arrays.asList(orderTableRequest1,orderTableRequest2);
    }

    @DisplayName("dto와 JPA를 사용하여 단체 지정을 할 수 있다")
    @Test
    void createTest() {

        //when
        ExtractableResponse<Response> response = 단체_지정_요청(tableGroupRequest);

        //then
        정상_등록(response);
        TableGroupResponse tableGroupResponse = response.as(TableGroupResponse.class);
        assertThat(tableGroupResponse.getCreatedDate()).isNotNull();
        assertThat(tableGroupResponse.getOrderTables().size()).isGreaterThan(0);
    }


    @DisplayName("dto와 JPA를 사용하여 단체 지정을 해제할 수 있다")
    @Test
    void unGroupTest() {
        //given
        ExtractableResponse<Response> savedResponse = 단체_지정_요청(tableGroupRequest);
        TableGroupResponse tableGroupResponse = savedResponse.as(TableGroupResponse.class);

        //when
        ExtractableResponse<Response> response = 단체_지정_해제_요청(tableGroupResponse.getId());

        //then
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
        테이블_그룹_해제_확인(tableGroupResponse.getOrderTables());
    }

    private void 테이블_그룹_해제_확인(List<OrderTableResponse> tableGroupResponse) {
        ExtractableResponse<Response> response = 주문_테이블_조회_요청();
        List<Long> ids = response.jsonPath().getList(".", OrderTableResponse.class).stream()
                .filter(orderTableResponse -> orderTableResponse.getTableGroupId()==null).map(OrderTableResponse::getId)
                .collect(Collectors.toList());
        for (OrderTableResponse orderTableResponse : tableGroupResponse) {
            assertThat(ids).contains(orderTableResponse.getId());
        }
    }

    private ExtractableResponse<Response> 단체_지정_요청(TableGroupRequest tableGroupRequest) {
        return RestAssured
                .given().log().all()
                .body(tableGroupRequest)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().post("/api/table-groups/temp")
                .then().log().all()
                .extract();
    }

    private ExtractableResponse<Response> 단체_지정_해제_요청(Long tableGroupId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/{tableGroupId}/temp",tableGroupId)
                .then().log().all()
                .extract();
    }
}
