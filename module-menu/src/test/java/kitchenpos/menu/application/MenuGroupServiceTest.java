package kitchenpos.menu.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.menu.dto.MenuGroupRequest;
import kitchenpos.menu.dto.MenuGroupResponse;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.repository.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @BeforeEach
    void setUp() {
        menuGroupService = new MenuGroupService(menuGroupRepository);
    }

    @Test
    @DisplayName("메뉴 그룹을 생성한다.")
    void createMenuGroup() {
        // given
        final MenuGroup menuGroup = new MenuGroup(1L, "세트메뉴");
        final MenuGroupResponse expectedMenuGroupResponse = menuGroup.toMenuGroupResponse();
        when(menuGroupRepository.save(any())).thenReturn(menuGroup);
        // when
        final MenuGroupResponse actual = menuGroupService.create(new MenuGroupRequest("세트메뉴"));
        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual.getId()).isEqualTo(expectedMenuGroupResponse.getId()),
                () -> assertThat(actual.getName()).isEqualTo(expectedMenuGroupResponse.getName())
        );
    }

    @Test
    @DisplayName("메뉴 그룹들을 조회한다.")
    void searchMenuGroups() {
        // given
        final MenuGroup menuGroup1 = new MenuGroup(1L, "세트메뉴1");
        final MenuGroup menuGroup2 = new MenuGroup(2L, "세트메뉴2");
        when(menuGroupRepository.findAll()).thenReturn(Arrays.asList(menuGroup1, menuGroup2));
        // when
        final List<MenuGroupResponse> actual = menuGroupService.list();
        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).hasSize(2)
        );
    }
}
