package kitchenpos.order.domain;

import kitchenpos.order.domain.OrderLineItem;
import kitchenpos.order.domain.OrderLineItemBag;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("주문 항목 일급 컬렉션 테스트")
public class OrderLineItemBagTest {

    private static final Long 메뉴_id = 1L;
    private static final long 수량 = 1l;

    @DisplayName("생성 성공")
    @Test
    void 생성_성공() {
        //given:
        OrderLineItemBag 주문_항목_목록 = 주문_항목_목록(메뉴_id, 수량);
        //when, then:
        assertThat(주문_항목_목록).isEqualTo(OrderLineItemBag.from(Collections.singletonList(
                new OrderLineItem(메뉴_id, 수량))));
    }

    @DisplayName("메뉴 아이디 목록 조회 메서드 테스트")
    @Test
    void 메뉴_아이디_목록_조회_메서드_테스트() {
        OrderLineItemBag 주문_항목_목록 = 주문_항목_목록(메뉴_id, 수량);
        assertThat(주문_항목_목록.menuIds()).containsExactly(메뉴_id);
    }

    private static OrderLineItemBag 주문_항목_목록(Long 메뉴_id, long 수량) {
        return OrderLineItemBag.from(Collections.singletonList(
                new OrderLineItem(메뉴_id, 수량)
        ));
    }
}
