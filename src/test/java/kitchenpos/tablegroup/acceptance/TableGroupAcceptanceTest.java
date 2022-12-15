package kitchenpos.tablegroup.acceptance;

import static kitchenpos.ordertable.acceptance.TableRestAssured.주문_테이블_생성_요청;
import static kitchenpos.tablegroup.acceptance.TableGroupRestAssured.단체_지정_요청;
import static kitchenpos.tablegroup.acceptance.TableGroupRestAssured.단체_지정_해제_요청;
import static org.assertj.core.api.Assertions.assertThat;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import java.util.Arrays;
import kitchenpos.common.acceptance.AcceptanceTest;
import kitchenpos.ordertable.dto.OrderTableRequest;
import kitchenpos.ordertable.dto.OrderTableResponse;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 주문테이블A;
    private OrderTableResponse 주문테이블B;
    private TableGroupRequest 단체;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블A = 주문_테이블_생성_요청(OrderTableRequest.of(4, true)).as(OrderTableResponse.class);
        주문테이블B = 주문_테이블_생성_요청(OrderTableRequest.of(6, true)).as(OrderTableResponse.class);
        단체 = TableGroupRequest.from(Arrays.asList(주문테이블A.getId(), 주문테이블B.getId()));
    }

    @DisplayName("주문 테이블의 단체를 지정한다.")
    @Test
    void createTableGroup() {
        // when
        ExtractableResponse<Response> response = 단체_지정_요청(단체);

        // then
        단체_지정됨(response);
    }

    @DisplayName("단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        TableGroupResponse tableGroupResponse = 단체_지정_요청(단체).as(TableGroupResponse.class);

        // when
        ExtractableResponse<Response> response = 단체_지정_해제_요청(tableGroupResponse.getId());

        // then
        단체_지정_해제됨(response);
    }

    private static void 단체_지정됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.CREATED.value());
    }

    private void 단체_지정_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.NO_CONTENT.value());

    }
}
