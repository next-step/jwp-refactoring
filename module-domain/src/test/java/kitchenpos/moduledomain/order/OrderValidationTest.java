package kitchenpos.moduledomain.order;

import static java.util.Arrays.asList;
import static kitchenpos.moduledomain.common.MenuFixture.메뉴_반반치킨;
import static kitchenpos.moduledomain.common.MenuFixture.메뉴_양념치킨;
import static kitchenpos.moduledomain.common.MenuFixture.메뉴_후라이드;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import kitchenpos.moduledomain.common.exception.DomainMessage;
import kitchenpos.moduledomain.menu.MenuDao;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderValidationTest {

    @Mock
    private MenuDao menuDao;

    @InjectMocks
    private OrderValidation orderValidation;

    @Test
    void 주문상품의_숫자와_메뉴에_등록된_숫자가_다른경우_예외() {
        // mocking
        when(menuDao.countByIdIn(any(List.class))).thenReturn(3L);

        assertThatThrownBy(() -> {
            List<Long> menuIds = asList(메뉴_양념치킨().getId(), 메뉴_반반치킨().getId(), 메뉴_후라이드().getId());
            orderValidation.validSizeIsNotEquals(menuIds, 2);
        }).isInstanceOf(IllegalArgumentException.class)
            .hasMessage(DomainMessage.ORDER_SIZE_IS_NOT_EQUALS.getMessage());
    }
}