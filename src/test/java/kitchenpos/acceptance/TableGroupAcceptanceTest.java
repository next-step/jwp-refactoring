package kitchenpos.acceptance;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.domain.OrderTable;
import kitchenpos.dto.OrderTableResponse;
import kitchenpos.dto.TableGroupRequest;
import kitchenpos.dto.TableGroupResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static kitchenpos.acceptance.OrderTableAcceptanceStep.등록된_주문_테이블;
import static kitchenpos.acceptance.TableGroupAcceptanceStep.*;
import static kitchenpos.fixture.OrderTableTestFixture.*;
import static kitchenpos.fixture.TableGroupTestFixture.테이블그룹요청;

@DisplayName("단체 지정 관련 인수 테스트")
public class TableGroupAcceptanceTest extends AcceptanceTest {

    private OrderTableResponse 주문테이블1;
    private OrderTableResponse 주문테이블2;
    private TableGroupRequest 단체1;

    @BeforeEach
    public void setUp() {
        super.setUp();
        주문테이블1 = 등록된_주문_테이블(주문테이블(1L, null, 10, true)).as(OrderTableResponse.class);
        주문테이블2 = 등록된_주문_테이블(주문테이블(2L, null, 20, true)).as(OrderTableResponse.class);
        OrderTable orderTable1 = setId(주문테이블1.getId(), OrderTable.of(null, 10, true));
        OrderTable orderTable2 = setId(주문테이블2.getId(), OrderTable.of(null, 10, true));

        단체1 = 테이블그룹요청(주문정보요청목록(Arrays.asList(orderTable1, orderTable2)));
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
