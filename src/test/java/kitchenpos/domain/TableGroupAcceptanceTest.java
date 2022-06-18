package kitchenpos.domain;

import static kitchenpos.domain.TableAcceptanceTestMethod.*;
import static kitchenpos.domain.TableGroupAcceptanceTestMethod.*;

import io.restassured.response.ExtractableResponse;
import io.restassured.response.Response;
import kitchenpos.AcceptanceTest;
import kitchenpos.application.fixture.OrderTableFixtureFactory;
import kitchenpos.application.fixture.TableGroupFixtureFactory;
import kitchenpos.table.domain.OrderTable;
import kitchenpos.tablegroup.domain.TableGroup;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("단체 관련 인수테스트")
class TableGroupAcceptanceTest extends AcceptanceTest {

    private TableGroup 단체_1;
    private OrderTable 주문_1_테이블;
    private OrderTable 주문_2_테이블;

    @BeforeEach
    public void setUp() {
        super.setUp();

        단체_1 = TableGroupFixtureFactory.create();

        주문_1_테이블 = OrderTableFixtureFactory.create(true);
        주문_2_테이블 = OrderTableFixtureFactory.create(true);

        주문_1_테이블 = 테이블_등록되어_있음(주문_1_테이블).as(OrderTable.class);
        주문_2_테이블 = 테이블_등록되어_있음(주문_2_테이블).as(OrderTable.class);
    }

    @DisplayName("단체를 지정할 수 있다.")
    @Test
    void create01() {
        // given
        단체_1.setOrderTables(Lists.newArrayList(주문_1_테이블, 주문_2_테이블));

        // when
        ExtractableResponse<Response> response = 단체_등록_요청(단체_1);

        // then
        단체_등록됨(response);
    }

    @DisplayName("단체를 해제할 수 있다.")
    @Test
    void change01() {
        // given
        단체_1.setOrderTables(Lists.newArrayList(주문_1_테이블, 주문_2_테이블));
        TableGroup createdTableGroup = 단체_등록되어_있음(단체_1).as(TableGroup.class);

        // when
        ExtractableResponse<Response> response = 단체_해제_요청(createdTableGroup.getId());

        // then
        단체_해제됨(response);
    }
}