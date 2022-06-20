package kitchenpos;

import kitchenpos.dto.OrderTableResponse;

import static kitchenpos.MenuAcceptanceUtil.신메뉴_강정치킨_가져오기;
import static kitchenpos.OrderAcceptanceTest.주문_등록됨;
import static kitchenpos.TableAcceptanceTest.테이블_등록됨;

public final class TableAcceptanceUtil {

    private TableAcceptanceUtil() {
    }

    public static OrderTableResponse 주문이_들어간_테이블_가져오기() {
        OrderTableResponse 주문이_들어간_테이블 = 테이블_등록됨(false, 5);
        주문_등록됨(주문이_들어간_테이블, 신메뉴_강정치킨_가져오기());
        return 주문이_들어간_테이블;
    }
}
