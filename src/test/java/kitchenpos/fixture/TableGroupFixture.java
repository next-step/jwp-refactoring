package kitchenpos.fixture;

import kitchenpos.domain.TableGroup;

import java.time.LocalDateTime;

import static java.util.Arrays.asList;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_1번쨰_빈_테이블;
import static kitchenpos.fixture.OrderTableFixture.주문_테이블_2번쨰_빈_테이블;

public class TableGroupFixture {
    public static TableGroup 테이블_그룹_1번 = new TableGroup(1L, LocalDateTime.now()
            , asList(
            주문_테이블_1번쨰_빈_테이블,
            주문_테이블_2번쨰_빈_테이블
    ));
    public static TableGroup 테이블_그룹_2번 = new TableGroup(1L, LocalDateTime.now()
            , asList(
            주문_테이블_1번쨰_빈_테이블,
            주문_테이블_2번쨰_빈_테이블
    ));
}
