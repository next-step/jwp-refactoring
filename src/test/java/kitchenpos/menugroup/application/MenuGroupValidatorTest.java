package kitchenpos.menugroup.application;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.menugroup.domain.MenuGroupRepository;
import kitchenpos.menugroup.exception.MenuGroupNotFoundException;

@ExtendWith(MockitoExtension.class)
class MenuGroupValidatorTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuGroupValidator menuGroupValidator;

    @Test
    @DisplayName("존재하지 않는 메뉴그룹 ID일 경우 예외처리")
    void validateExistsMenuGroupBYId() {
        // given
        given(menuGroupRepository.findById(anyLong())).willReturn(Optional.empty());

        // then
        assertThatThrownBy(() -> menuGroupValidator.validateExistsMenuGroupById(1L))
                .isInstanceOf(MenuGroupNotFoundException.class)
                .hasMessage("조회된 메뉴 그룹이 없습니다.");
    }
}
