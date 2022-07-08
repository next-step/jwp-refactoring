package kitchenpos.application;

import static kitchenpos.utils.generator.MenuGroupFixtureGenerator.메뉴_그룹_목록_생성;
import static kitchenpos.utils.generator.MenuGroupFixtureGenerator.메뉴_그룹_생성_요청_객체;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.verify;

import java.util.List;
import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.AdditionalAnswers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("Service:MenuGroup")
class MenuGroupServiceTest {

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    public void createMenuGroup() {
        // Given
        final MenuGroupRequest 오늘의_메뉴 = 메뉴_그룹_생성_요청_객체("오늘의 메뉴");
        given(menuGroupRepository.save(any(MenuGroup.class))).will(AdditionalAnswers.returnsFirstArg());

        // When
        menuGroupService.create(오늘의_메뉴);

        // Then
        verify(menuGroupRepository).save(any(MenuGroup.class));
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    public void getMenuGroups() {
        // Given
        final int generateMenuGroupCount = 5;
        List<MenuGroup> givenMenuGroups = 메뉴_그룹_목록_생성(generateMenuGroupCount);
        given(menuGroupRepository.findAll()).willReturn(givenMenuGroups);

        // When
        List<MenuGroupResponse> actualMenuGroups = menuGroupService.list();

        // Then
        verify(menuGroupRepository).findAll();
        assertThat(actualMenuGroups).hasSize(generateMenuGroupCount);
    }
}
