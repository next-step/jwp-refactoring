package kitchenpos.menuGroup.application;

import kitchenpos.menuGroup.domain.MenuGroup;
import kitchenpos.menuGroup.domain.MenuGroupRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static kitchenpos.menuGroup.domain.MenuGroupTest.메뉴그룹_생성;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroup menuGroup = 메뉴그룹_생성("menuGroup");

        Mockito.when(menuGroupRepository.save(menuGroup)).thenReturn(메뉴그룹_생성(1L, "menuGroup"));

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertAll(
                () -> assertThat(savedMenuGroup.getId()).isNotNull(),
                () -> assertThat(savedMenuGroup.getName()).isEqualTo(menuGroup.getName())
        );

    }

    @DisplayName("메뉴 그룹 목록을 조회한다. ")
    @Test
    void listMenuGroup() {
        // given
        MenuGroup menuGroup1 = 메뉴그룹_생성(1L, "menuGroup1");
        MenuGroup menuGroup2 = 메뉴그룹_생성(2L, "menuGroup2");

        Mockito.when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(menuGroup1, menuGroup2));

        // when
        List<MenuGroup> savedMenuGroups = menuGroupService.list();

        // then
        assertThat(savedMenuGroups).containsExactly(menuGroup1, menuGroup2);
    }
}
