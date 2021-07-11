package kitchenpos.tablegroup;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tablegroup.domain.TableGroup;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.net.URI;
import java.util.Arrays;
import java.util.List;

import static kitchenpos.table.tableAcceptanceTest.주문_테이블_등록_되어있음;
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
}
