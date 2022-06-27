package kitchenpos.fixture.acceptance;

import kitchenpos.acceptance.TableAcceptanceTest;
import kitchenpos.domain.OrderTable;

public class AcceptanceTestOrderTableFixture {
    public final OrderTable 테이블;
    public final OrderTable 빈_테이블1;
    public final OrderTable 빈_테이블2;

    public AcceptanceTestOrderTableFixture() {
        테이블 = TableAcceptanceTest.주문_테이블_생성_요청(4, false).as(OrderTable.class);
        빈_테이블1 = TableAcceptanceTest.주문_테이블_생성_요청(0, true).as(OrderTable.class);
        빈_테이블2 = TableAcceptanceTest.주문_테이블_생성_요청(0, true).as(OrderTable.class);
    }
}
