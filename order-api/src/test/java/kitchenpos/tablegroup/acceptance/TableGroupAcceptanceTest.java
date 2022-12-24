package kitchenpos.tablegroup.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.common.acceptance.AcceptanceTest;
import kitchenpos.ordertable.acceptance.OrderTableAcceptanceStep;
import kitchenpos.ordertable.fixture.OrderTableTestFixture;
import kitchenpos.tablegroup.fixture.TableGroupTestFixture;
import kitchenpos.ordertable.domain.OrderTable;
import kitchenpos.ordertable.dto.OrderTableResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import kitchenpos.tablegroup.dto.TableGroupRequest;
import kitchenpos.tablegroup.dto.TableGroupResponse;

import java.util.Arrays;

import static kitchenpos.tablegroup.acceptance.TableGroupAcceptanceStep.*;

@DisplayName("단체 지정 관련 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 주문테이블1;
    private OrderTableResponse 주문테이블2;
    private TableGroupRequest 단체1;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블1 = OrderTableAcceptanceStep.등록된_주문_테이블(OrderTableTestFixture.주문테이블(1L, null, 10, true)).as(OrderTableResponse.class);
        주문테이블2 = OrderTableAcceptanceStep.등록된_주문_테이블(OrderTableTestFixture.주문테이블(2L, null, 20, true)).as(OrderTableResponse.class);
        OrderTable orderTable1 = OrderTableTestFixture.setId(주문테이블1.getId(), OrderTable.of(10, true));
        OrderTable orderTable2 = OrderTableTestFixture.setId(주문테이블2.getId(), OrderTable.of(10, true));

        단체1 = TableGroupTestFixture.테이블그룹요청(OrderTableTestFixture.주문정보요청목록(Arrays.asList(orderTable1, orderTable2)));
    }

    @DisplayName("주문 테이블들에 대해 단체를 지정한다.")
    @Test
    void create() {
        // when
        ExtractableResponse<Response> response = 단체_지정_요청(단체1);

        // then
        단체_지정됨(response);
    }

    @DisplayName("등록된 테이블 단체 지정을 해제한다.")
    @Test
    void ungroup() {
        // given
        TableGroupResponse tableGroup = 지정된_단체(단체1).as(TableGroupResponse.class);

        // when
        ExtractableResponse<Response> response = 단체_해제_요청(tableGroup.getId());

        // then
        단체_해제됨(response);
    }
}
