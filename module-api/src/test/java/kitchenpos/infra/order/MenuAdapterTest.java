package kitchenpos.infra.order;

import kitchenpos.domain.menu.MenuRepository;
import kitchenpos.domain.order.OrderLineItem;
import kitchenpos.domain.order.exceptions.MenuEntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuAdapterTest {
    private MenuAdapter menuAdapter;

    @Mock
    private MenuRepository menuRepository;

    @BeforeEach
    void setup() {
        menuAdapter = new MenuAdapter(menuRepository);
    }

    @DisplayName("제시된 주문 항목들 중 하나라도 존재하지 않으면 예외 발생")
    @Test
    void isExistMenusTest() {
        // given
        List<OrderLineItem> orderLineItems = Arrays.asList(new OrderLineItem(1L, 1L), new OrderLineItem(2L, 2L));
        given(menuRepository.countByIdIn(Arrays.asList(1L, 2L))).willReturn(1);

        // when, then
        assertThatThrownBy(() -> menuAdapter.isMenuExists(orderLineItems))
                .isInstanceOf(MenuEntityNotFoundException.class)
                .hasMessage("존재하지 않는 메뉴가 있습니다.");
    }
}
