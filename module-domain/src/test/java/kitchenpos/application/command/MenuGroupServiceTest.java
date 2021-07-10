package kitchenpos.application.command;

import kitchenpos.domain.menu.MenuGroupCreate;
import kitchenpos.domain.menu.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.internal.verification.VerificationModeFactory;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;

    private MenuGroupService menuGroupService;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @Test
    @DisplayName("create - 정상적인 메뉴 그룹 저장")
    void 정상적인_메뉴_그룹_저장() {
        // given
        MenuGroupCreate menuGroupCreate = new MenuGroupCreate("Hello");

        // when
        when(menuGroupRepository.save(any())).thenAnswer(i -> i.getArgument(0));

        menuGroupService.create(menuGroupCreate);

        // then

        verify(menuGroupRepository, VerificationModeFactory.times(1))
                .save(any());
    }
}