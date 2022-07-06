package kitchenpos.menu.domain;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import kitchenpos.menu.dao.MenuRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuValidatorTest {

    @InjectMocks
    MenuValidator menuValidator;

    @Mock
    MenuRepository menuRepository;

    @Test
    @DisplayName("주문내역의 메뉴가 모두 존재하지 않으면 오류를 반환한다")
    void create_nonMenuError() {
        // given
        given(menuRepository.countByIdIn(any())).willReturn(1L);

        // when & then
        assertThatIllegalArgumentException().isThrownBy(
                () -> menuValidator.validateOrderLineItemsCheck(Arrays.asList(1L, 2L))
        ).withMessageContaining("존재하지 않는 메뉴입니다.");
    }
}
