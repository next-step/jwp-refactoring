package kitchenpos.validator.menu.impl;

import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Optional;
import kitchenpos.menugroup.repository.MenuGroupRepository;
import org.assertj.core.api.ThrowableAssert.ThrowingCallable;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AlreadyGroupedMenuValidatorTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @Test
    void 메뉴_등록시_등록되어_있는_메뉴_그룹만_지정할_수_있다() {
        AlreadyGroupedMenuValidator validator = new AlreadyGroupedMenuValidator(menuGroupRepository);
        given(menuGroupRepository.findById(any())).willReturn(Optional.empty());

        ThrowingCallable 등록되지_않은_메뉴_그룹을_지정_한_경우 = () -> validator.validate(1L);

        assertThatIllegalArgumentException().isThrownBy(등록되지_않은_메뉴_그룹을_지정_한_경우);
    }
}
