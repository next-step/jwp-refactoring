package kitchenpos.Acceptance.tableGroup;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.Acceptance.AcceptanceTest;
import kitchenpos.order.domain.OrderStatus;
import kitchenpos.order.dto.OrderCreateRequest;
import kitchenpos.order.dto.OrderLineItemRequest;
import kitchenpos.order.dto.OrderResponse;
import kitchenpos.table.dto.OrderTableResponse;
import kitchenpos.tableGroup.dto.TableGroupCreateRequest;
import kitchenpos.tableGroup.dto.TableGroupResponse;
import org.apache.http.HttpStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.order.OrderGenerator.*;
import static kitchenpos.table.TableGenerator.*;
import static org.assertj.core.api.Assertions.assertThat;

public class TableGroupAcceptanceTest extends AcceptanceTest {

    private List<Long> 주문_테이블_아이디들 = new ArrayList<>();

    @BeforeEach
    public void setUp() {
        super.setUp();

        주문_테이블_아이디들.clear();
        for(int i = 0; i < 5; i++) {
            주문_테이블_아이디들.add(테이블_생성_API_호출(주문_테이블_생성_요청(i)).as(OrderTableResponse.class).getId());
        }
    }

    @DisplayName("단체 지정 생성 시 해당 그룹에 속한 테이블의 수가 2개 미만이면 예외가 발생해야 한다")
    @Test
    void createTableGroupByNotFillMinimumTableCount() {
        // when
        ExtractableResponse<Response> 테이블_그룹에_속한_테이블이_한개_요청_결과 = 테이블_그룹_생성_API_호출(
                테이블_그룹_생성_요청(Collections.singletonList(주문_테이블_아이디들.get(0)))
        );
        ExtractableResponse<Response> 테이블_그룹에_속한_테이블이_없는_요청_결과 = 테이블_그룹_생성_API_호출(
                테이블_그룹_생성_요청(Collections.emptyList())
        );

        // then
        테이블_그룹_생성_요청_실패됨(테이블_그룹에_속한_테이블이_한개_요청_결과);
        테이블_그룹_생성_요청_실패됨(테이블_그룹에_속한_테이블이_없는_요청_결과);
    }

    @DisplayName("단체 지정의 테이블 수와 실제 저장된 테이블 수가 다를 경우 예외가 발생해야 한다")
    @Test
    void createTableGroupByNotMatchedTableCountTest() {
        // when
        ExtractableResponse<Response> 생성_요청_결과 = 테이블_그룹_생성_API_호출(
                테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), -1L))
        );

        // then
        테이블_그룹_생성_요청_실패됨(생성_요청_결과);
    }

    @DisplayName("단체 지정 생성 시 포함된 테이블 중 빈 테이블 아니거나 이미 다른 단체 지정에 속해 있는 테이블이 포함되어 있으면 예외가 발생해야 한다")
    @Test
    void createTableByContainAlreadyBelongOrNotEmptyTableTest() {
        // given
        테이블_그룹_생성_API_호출(테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), 주문_테이블_아이디들.get(1))));
        빈_테이블_변경_API_호출(주문_테이블_아이디들.get(2), false);

        // when
        ExtractableResponse<Response> 이미_테이블_그룹에_속한_테이블이_포함된_테이블_그룹_생성_요청_결과 = 테이블_그룹_생성_API_호출(
                테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), 주문_테이블_아이디들.get(3)))
        );
        ExtractableResponse<Response> 빈_상태가_아닌_테이블이_포함된_테이블_그룹_생성_요청_결과 = 테이블_그룹_생성_API_호출(
                테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(2), 주문_테이블_아이디들.get(4)))
        );

        // then
        테이블_그룹_생성_요청_실패됨(이미_테이블_그룹에_속한_테이블이_포함된_테이블_그룹_생성_요청_결과);
        테이블_그룹_생성_요청_실패됨(빈_상태가_아닌_테이블이_포함된_테이블_그룹_생성_요청_결과);
    }

    @DisplayName("정상 상태의 단체 지정 생성 요청 시 정상 생성 되어야 한다")
    @Test
    void createTableGroupTest() {
        // given
        TableGroupCreateRequest 테이블_그룹_생성_요청 = 테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), 주문_테이블_아이디들.get(1)));

        // when
        ExtractableResponse<Response> 테이블_그룹_생성_요청_결과 = 테이블_그룹_생성_API_호출(테이블_그룹_생성_요청);

        // then
        테이블_그룹_생성_요청_성공됨(테이블_그룹_생성_요청_결과, 테이블_그룹_생성_요청);
    }

    @DisplayName("단체 지정에 포함되어 있는 테이블 중 요리중 또는 식사중 상태인 테이블이 존재하는 테이블 그룹을 해제하면 예외가 발생해야 한다")
    @ParameterizedTest
    @EnumSource(value = OrderStatus.class, names = { "COOKING", "MEAL" })
    void ungroupByIncludeCookingOrMealStatusTableTest(OrderStatus orderStatus) {
        // given
        Long 테이블_그룹_아이디 = 테이블_그룹_생성_API_호출(
                테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), 주문_테이블_아이디들.get(1)))
        ).body().jsonPath().getLong("id");
        OrderLineItemRequest 주문_물품_생성_요청 = 주문_물품_생성_요청(메뉴_아이디, 1L);
        OrderCreateRequest 주문_생성_요청 = 주문_생성_요청(주문_테이블_아이디들.get(0), Collections.singletonList(주문_물품_생성_요청));
        Long 생성된_주문_아이디 = 주문_생성_API_요청(주문_생성_요청).as(OrderResponse.class).getId();
        주문_상태_변경_API_요청(생성된_주문_아이디, orderStatus);

        // when
        ExtractableResponse<Response> 테이블_그룹_해제_요청_결과 = 테이블_그룹_해제_API_호출(테이블_그룹_아이디);

        // then
        테이블_그룹_해제_요청_실패됨(테이블_그룹_해제_요청_결과);
    }

    @DisplayName("정상 상태의 단체 지정 해제 요청시 정상 해제 되어야 한다")
    @Test
    void ungroupTestGroupTest() {
        // given
        Long 테이블_그룹_아이디 = 테이블_그룹_생성_API_호출(
                테이블_그룹_생성_요청(Arrays.asList(주문_테이블_아이디들.get(0), 주문_테이블_아이디들.get(1)))
        ).body().jsonPath().getLong("id");

        // when
        ExtractableResponse<Response> 테이블_그룹_해제_요청_결과 = 테이블_그룹_해제_API_호출(테이블_그룹_아이디);

        // then
        테이블_그룹_해제됨(테이블_그룹_해제_요청_결과);
    }

    void 테이블_그룹_생성_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    void 테이블_그룹_생성_요청_성공됨(ExtractableResponse<Response> response, TableGroupCreateRequest request) {
        TableGroupResponse tableGroup = response.as(TableGroupResponse.class);
        List<Long> tableGroupBelongOrderTableIds = response.body().jsonPath().getList("orderTableResponses.id", Long.class);
        List<Long> expectedTableIdes = request.getOrderTables();

        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_CREATED);
        assertThat(tableGroup.getOrderTableResponses().size()).isEqualTo(request.getOrderTables().size());
        assertThat(tableGroupBelongOrderTableIds).containsAll(expectedTableIdes);
    }

    void 테이블_그룹_해제_요청_실패됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_INTERNAL_SERVER_ERROR);
    }

    void 테이블_그룹_해제됨(ExtractableResponse<Response> response) {
        assertThat(response.statusCode()).isEqualTo(HttpStatus.SC_NO_CONTENT);
    }
}
