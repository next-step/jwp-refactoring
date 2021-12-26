package kitchenpos.validator;

import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.domain.MenuRepository;
import kitchenpos.order.exception.InvalidMenuInOrderLineItems;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class OrderMenuValidatorTest {

    @Mock
    private MenuRepository menuRepository;

    @InjectMocks
    private OrderMenuValidator orderMenuValidator;

    @DisplayName("중복되는 메뉴 ID가 존재해서는 안된다.")
    @Test
    void validateOrderLineItems1() {
        //given
        List<Long> menuIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 6L);
        메뉴_개수_조회_모킹(menuIds, menuIds.size() - 1);

        //when, then
        Assertions.assertThatThrownBy(() -> orderMenuValidator.validateOrderLineItems(menuIds))
            .isInstanceOf(InvalidMenuInOrderLineItems.class);
    }

    @DisplayName("없는 메뉴 ID가 존재해서는 안된다.")
    @Test
    void validateOrderLineItems2() {
        //given : 999L 은 없는 메뉴라고 가정한다.
        List<Long> menuIds = Arrays.asList(1L, 2L, 3L, 4L, 5L, 6L, 999L);
        메뉴_개수_조회_모킹(menuIds, menuIds.size() - 1);

        //when, then
        Assertions.assertThatThrownBy(() -> orderMenuValidator.validateOrderLineItems(menuIds))
            .isInstanceOf(InvalidMenuInOrderLineItems.class);
    }

    private void 메뉴_개수_조회_모킹(List<Long> menuIds, long expectedResult) {
        given(menuRepository.countByIdIn(menuIds))
            .willReturn(expectedResult);
    }
}
