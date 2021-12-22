package kitchenpos.acceptance;

import static java.util.Arrays.asList;
import static kitchenpos.acceptance.OrderTableAcceptanceTest.주문테이블_생성_요청;
import static kitchenpos.utils.StatusValidation.삭제됨;
import static kitchenpos.utils.StatusValidation.생성됨;
import static kitchenpos.utils.TestFactory.delete;
import static kitchenpos.utils.TestFactory.post;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.time.LocalDateTime;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TableGroupAcceptanceTest extends AcceptanceTest {


    public static final String TABLE_GROUP_BASE_URL = "/table-groups";
    private OrderTableRequest 주문_테이블1;
    private OrderTableRequest 주문_테이블2;

    @BeforeEach
    public void setUp() {
        super.setUp();
        OrderTableResponse createTable1 = 주문테이블_생성_요청(
            new OrderTableRequest(0, true)).as(
            OrderTableResponse.class);
        OrderTableResponse createTable2 = 주문테이블_생성_요청(
            new OrderTableRequest(0, true)).as(
            OrderTableResponse.class);

        주문_테이블1 = new OrderTableRequest(1L, createTable1.getNumberOfGuests(), false);
        주문_테이블2 = new OrderTableRequest(2L, createTable2.getNumberOfGuests(), false);
    }

    @Test
    void 주문테이블_단체지정_생성() {

        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(asList(주문_테이블1, 주문_테이블2));

        // when
        ExtractableResponse<Response> response = 단체지정_생성_요청(tableGroupRequest);

        // then
        단체지정_생성됨(response);
    }

    @Test
    void 단체지정을_취소할_수_있다() {

        // given
        TableGroupRequest tableGroupRequest = new TableGroupRequest(asList(주문_테이블1, 주문_테이블2));

        // when
        TableGroupResponse createResponse = 단체지정_생성_요청(tableGroupRequest).as(
            TableGroupResponse.class);

        // then
        ExtractableResponse<Response> response = 단체지정_취소_요청(createResponse.getId());
        단체지정_취소됨(response);

    }

    public static ExtractableResponse<Response> 단체지정_생성_요청(TableGroupRequest tableGroupRequest) {
        return post(TABLE_GROUP_BASE_URL, tableGroupRequest);
    }

    public static void 단체지정_생성됨(ExtractableResponse<Response> response) {
        생성됨(response);
    }

    public static ExtractableResponse<Response> 단체지정_취소_요청(Long id) {
        return delete(TABLE_GROUP_BASE_URL+"/{tableGroupId}","tableGroupId", id);
    }

    public static void 단체지정_취소됨(ExtractableResponse<Response> response) {
        삭제됨(response);
    }

}
