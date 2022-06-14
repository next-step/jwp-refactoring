package kitchenpos;

import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.OrderTable;
import kitchenpos.domain.Product;
import java.math.BigDecimal;
import static kitchenpos.MenuAcceptanceTest.메뉴_등록됨;
import static kitchenpos.MenuGroupAcceptanceTest.메뉴_그룹_등록됨;
import static kitchenpos.OrderAcceptanceTest.주문_등록됨;
import static kitchenpos.ProductAcceptanceTest.상품_등록됨;
import static kitchenpos.TableAcceptanceTest.테이블_등록됨;

public final class TableAcceptanceUtil {

    private TableAcceptanceUtil() {
    }

    public static OrderTable 주문이_들어간_테이블_가져오기() {
        OrderTable 주문이_들어간_테이블 = 테이블_등록됨(false, 5);
        Product 강정치킨_상품 = 상품_등록됨("강정치킨", BigDecimal.valueOf(15_000L));
        MenuGroup 신메뉴 = 메뉴_그룹_등록됨("신메뉴");
        Menu 강정치킨 = 메뉴_등록됨("강정치킨", BigDecimal.valueOf(15_000L), 신메뉴.getId(), 강정치킨_상품);
        주문_등록됨(주문이_들어간_테이블, 강정치킨);
        return 주문이_들어간_테이블;
    }

    public static OrderTable 존재하지_않는_테이블_가져오기() {
        OrderTable 존재하지_않는_테이블 = new OrderTable();
        존재하지_않는_테이블.setId(Long.MAX_VALUE);
        return 존재하지_않는_테이블;
    }
}
