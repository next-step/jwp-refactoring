package kitchenpos.order.domain;

import static kitchenpos.helper.MenuFixtures.메뉴_만들기;
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.Menus;
import kitchenpos.order.dto.OrderLineItemRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("주문항목들 관련 Domain 단위 테스트")
class OrderLineItemsTest {

    @DisplayName("주문 항목이 비어있는 경우 주문항목들을 생성할 수 없다.")
    @Test
    void create() {
        //given
        Menu 후라이드치킨 = 메뉴_만들기(1L, "후라이드치킨", 10000);
        Menu 피자 = 메뉴_만들기(2L, "피자", 20000);
        List<OrderLineItemRequest> requests = Collections.emptyList();

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> OrderLineItems.create(requests, new Menus(Arrays.asList(후라이드치킨, 피자))));
    }
}
