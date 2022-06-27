package kitchenpos.acceptance.tablegroup;

import static kitchenpos.acceptance.table.TableAcceptanceTestMethod.테이블_등록되어_있음;
import static kitchenpos.acceptance.tablegroup.TableGroupAcceptanceTestMethod.단체_등록_요청;
import static kitchenpos.acceptance.tablegroup.TableGroupAcceptanceTestMethod.단체_등록되어_있음;
import static kitchenpos.acceptance.tablegroup.TableGroupAcceptanceTestMethod.단체_등록됨;
import static kitchenpos.acceptance.tablegroup.TableGroupAcceptanceTestMethod.단체_해제_요청;
import static kitchenpos.acceptance.tablegroup.TableGroupAcceptanceTestMethod.단체_해제됨;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.table.dto.OrderTableRequest;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.table.dto.TableGroupRequest;
import kitchenpos.table.dto.TableGroupResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체 관련 인수테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private TableGroupRequest tableGroupRequest;
    private OrderTableResponse 주문_1_테이블;
    private OrderTableResponse 주문_2_테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문_1_테이블 = 테이블_등록되어_있음(OrderTableRequest.of(2, true)).as(OrderTableResponse.class);
        주문_2_테이블 = 테이블_등록되어_있음(OrderTableRequest.of(4, true)).as(OrderTableResponse.class);
        tableGroupRequest = TableGroupRequest.from(Lists.newArrayList(주문_1_테이블.getId(), 주문_2_테이블.getId()));
    }

    @DisplayName("단체를 지정할 수 있다.")
    @Test
    void create01() {
        // when
        ExtractableResponse<Response> response = 단체_등록_요청(tableGroupRequest);

        // then
        단체_등록됨(response);
    }

    @DisplayName("단체를 해제할 수 있다.")
    @Test
    void change01() {
        // given
        TableGroupResponse 단체_등록됨_response = 단체_등록되어_있음(tableGroupRequest).as(TableGroupResponse.class);

        // when
        ExtractableResponse<Response> response = 단체_해제_요청(단체_등록됨_response.getId());

        // then
        단체_해제됨(response);
    }
}