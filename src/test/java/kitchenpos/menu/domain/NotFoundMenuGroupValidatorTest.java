package kitchenpos.menu.domain;

import kitchenpos.menu.domain.validator.ExistMenuGroupMenuCreateValidator;
import kitchenpos.menugroup.infra.MenuGroupRepository;
import org.assertj.core.api.ThrowableAssert;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 존재 유효성 검사 테스트")
@ExtendWith(MockitoExtension.class)
class NotFoundMenuGroupValidatorTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private ExistMenuGroupMenuCreateValidator notFoundMenuGroupValidator;

    @DisplayName("존재하지 않을 경우 유효하지 못하다.")
    @Test
    void validateFail() {
        // given
        given(menuGroupRepository.existsById(anyLong())).willReturn(false);
        // when
        final ThrowableAssert.ThrowingCallable throwingCallable = () -> notFoundMenuGroupValidator.validate(any());
        // then
        assertThatThrownBy(throwingCallable).isInstanceOf(IllegalArgumentException.class);
    }


    @DisplayName("존재하지 않을 경우 유효하다..")
    @Test
    void validateSuccess() {
        // given
        given(menuGroupRepository.existsById(anyLong())).willReturn(true);
        // when
        final Executable executable = () -> notFoundMenuGroupValidator.validate(any());
        // then
        assertDoesNotThrow(executable);
    }
}