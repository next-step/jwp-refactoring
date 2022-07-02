package kitchenpos.order.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import kitchenpos.helper.MenuBuilder;
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
        Menu 후라이드치킨 = MenuBuilder.builder().id(1L).name("후라이드치킨").price(10000).build();
        Menu 피자 = MenuBuilder.builder().id(2L).name("피자").price(20000).build();
        List<OrderLineItemRequest> requests = Collections.emptyList();

        //when then
        assertThatIllegalArgumentException()
                .isThrownBy(() -> OrderLineItems.create(requests, new Menus(Arrays.asList(후라이드치킨, 피자))))
                .withMessageContaining("주문 항목이 비어있습니다");
    }
}
