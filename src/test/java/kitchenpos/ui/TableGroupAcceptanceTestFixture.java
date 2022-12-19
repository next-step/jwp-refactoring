package kitchenpos.ui;

import static kitchenpos.ui.TableAcceptanceTestFixture.주문_테이블_생성_되어있음;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.RestAssured;
import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import kitchenpos.AcceptanceTest;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

public class TableGroupAcceptanceTestFixture extends AcceptanceTest {

    public OrderTable 주문_테이블_1;
    public OrderTable 주문_테이블_2;
    public OrderTable 주문_테이블_3;
    public OrderTable 주문_테이블_4;
    public List<OrderTable> 주문_테이블_목록;
    public List<OrderTable> 주문_테이블_목록_2;
    public TableGroupRequest 단체_1;
    public TableGroupRequest 단체_2;

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블_1 = 주문_테이블_생성_되어있음(new OrderTable(null, null, 0, true));
        주문_테이블_2 = 주문_테이블_생성_되어있음(new OrderTable(null, null, 0, true));
        주문_테이블_목록 = Arrays.asList(주문_테이블_1, 주문_테이블_2);
        단체_1 = createTableGroupRequest(주문_테이블_목록);

        주문_테이블_3 = 주문_테이블_생성_되어있음(new OrderTable(null, null, 0, true));
        주문_테이블_4 = 주문_테이블_생성_되어있음(new OrderTable(null, null, 0, true));
        주문_테이블_목록_2 = Arrays.asList(주문_테이블_3, 주문_테이블_4);
        단체_2 = createTableGroupRequest(주문_테이블_목록_2);
    }

    public static ExtractableResponse<Response> 단체_지정_생성_요청(TableGroupRequest tableGroupRequest) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .body(tableGroupRequest)
                .when().post("/api/table-groups")
                .then().log().all()
                .extract();
    }

    public static TableGroupResponse 단체_지정_생성_되어있음(TableGroupRequest tableGroupRequest) {
        return 단체_지정_정보(단체_지정_생성_요청(tableGroupRequest));
    }

    public static TableGroupResponse 단체_지정_정보(ExtractableResponse<Response> response) {
        return response.jsonPath().getObject("", TableGroupResponse.class);
    }

    public static void 단체_지정_생성됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    public static void 단체_지정_생성되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isIn(Arrays.asList(HttpStatus.BAD_REQUEST.value()
                , HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    public static ExtractableResponse<Response> 단체_지정_해제_요청(Long tableGroupId) {
        return RestAssured
                .given().log().all()
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .when().delete("/api/table-groups/" + tableGroupId)
                .then().log().all()
                .extract();
    }

    public static void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());
    }

    public static void 단체_지정_해제되지_않음(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isIn(Arrays.asList(HttpStatus.BAD_REQUEST.value()
                , HttpStatus.INTERNAL_SERVER_ERROR.value()));
    }

    public static boolean 주문_테이블_목록에_포함_확인(List<OrderTable> orderTables, Long orderTableId) {
        return orderTables.stream().anyMatch(orderTable -> orderTable.getId().equals(orderTableId));
    }

    public static TableGroupRequest createTableGroupRequest(List<OrderTable> orderTables) {
        return new TableGroupRequest(orderTables.stream()
                .map(OrderTable::getId)
                .collect(Collectors.toList()));
    }
}
