package kitchenpos.fixture.acceptance;

import java.util.Arrays;
import kitchenpos.acceptance.TableAcceptanceTest;
import kitchenpos.acceptance.TableGroupAcceptanceTest;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.TableGroup;

public class AcceptanceTestTableGroupFixture {
    public final OrderTable 단체_테이블1;
    public final OrderTable 단체_테이블2;
    public final TableGroup 단체;

    public AcceptanceTestTableGroupFixture() {
        단체_테이블1 = TableAcceptanceTest.주문_테이블_생성_요청(0, true).as(OrderTable.class);
        단체_테이블2 = TableAcceptanceTest.주문_테이블_생성_요청(0, true).as(OrderTable.class);
        단체 = TableGroupAcceptanceTest.테이블_단체_지정_요청(Arrays.asList(단체_테이블1, 단체_테이블2)).as(TableGroup.class);
    }
}
