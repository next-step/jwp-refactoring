package kitchenpos.fixture.acceptance;

import kitchenpos.table.acceptance.TableAcceptanceTest;
import kitchenpos.table.dto.OrderTableResponse;

public class AcceptanceTestOrderTableFixture {
    public final OrderTableResponse 테이블1;
    public final OrderTableResponse 테이블2;
    public final OrderTableResponse 빈_테이블1;
    public final OrderTableResponse 빈_테이블2;

    public AcceptanceTestOrderTableFixture() {
        테이블1 = TableAcceptanceTest.주문_테이블_생성_요청(4, false).as(OrderTableResponse.class);
        테이블2 = TableAcceptanceTest.주문_테이블_생성_요청(2, false).as(OrderTableResponse.class);
        빈_테이블1 = TableAcceptanceTest.주문_테이블_생성_요청(0, true).as(OrderTableResponse.class);
        빈_테이블2 = TableAcceptanceTest.주문_테이블_생성_요청(0, true).as(OrderTableResponse.class);
    }
}
