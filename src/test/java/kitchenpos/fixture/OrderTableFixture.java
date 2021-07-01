package kitchenpos.fixture;

import kitchenpos.domain.NumberOfGuest;
import kitchenpos.domain.Order;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Orders;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static kitchenpos.fixture.OrderFixture.*;
import static kitchenpos.fixture.TableGroupFixture.주문이_없는_테이블_그룹;

public class OrderTableFixture {
    public static OrderTable 미사용중인_테이블;
    public static OrderTable 미사용중인_테이블2;
    public static OrderTable 사용중인_1명_테이블;
    public static OrderTable 사용중인_2명_테이블;

    public static OrderTable 사용중인_1명_3건_결제완로;
    public static OrderTable 사용중인_1명_1건_결제완료_1건_식사;

    public static OrderTable 사용중인_1명_2건_결제완료1;
    public static OrderTable 사용중인_1명_2건_결제완료2;

    public static OrderTable 단체만_지정이_되어있고_빈_테이블;
    public static OrderTable 빈_테이블;

    public static OrderTable 사용중인_1명_1건_조리_1건_식사;


    public static void cleanUp() {

        List<Order> 결제완료 = Arrays.asList(결제완료1, 결제완료2, 결제완료3);
        List<Order> 결제완료_식사 = Arrays.asList(결제완료4, 식사1);
        List<Order> 결제완료2_1 = Arrays.asList(결제완료1, 결제완료2);
        List<Order> 결제완료2_2 = Arrays.asList(결제완료3, 결제완료4);

        미사용중인_테이블 = new OrderTable(1L, new NumberOfGuest(0), true);
        미사용중인_테이블2 = new OrderTable(2L, new NumberOfGuest(0), true);
        사용중인_1명_테이블 = new OrderTable(3L, 주문이_없는_테이블_그룹, Collections.emptyList(), new NumberOfGuest(1), false);
        사용중인_2명_테이블 = new OrderTable(4L, 주문이_없는_테이블_그룹, Collections.emptyList(), new NumberOfGuest(2), false);

        사용중인_1명_3건_결제완로 = new OrderTable(5L, 주문이_없는_테이블_그룹, new Orders(결제완료), new NumberOfGuest(1), false);
        사용중인_1명_1건_결제완료_1건_식사 = new OrderTable(6L, 주문이_없는_테이블_그룹, new Orders(결제완료_식사), new NumberOfGuest(1), false);

        사용중인_1명_2건_결제완료1 = new OrderTable(5L, 주문이_없는_테이블_그룹, new Orders(결제완료2_1), new NumberOfGuest(1), false);
        사용중인_1명_2건_결제완료2 = new OrderTable(6L, 주문이_없는_테이블_그룹, new Orders(결제완료2_2), new NumberOfGuest(1), false);

        단체만_지정이_되어있고_빈_테이블 = new OrderTable(7L, 주문이_없는_테이블_그룹, Arrays.asList(), new NumberOfGuest(1), true);
        빈_테이블 = new OrderTable(8L, null, Arrays.asList(), new NumberOfGuest(0), true);

        사용중인_1명_1건_조리_1건_식사 =  new OrderTable(9L, 주문이_없는_테이블_그룹, Arrays.asList(식사1, 조리1), new NumberOfGuest(1), false);
    }
}

