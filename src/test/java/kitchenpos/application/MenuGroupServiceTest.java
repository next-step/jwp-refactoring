package kitchenpos.application;

import kitchenpos.menu.application.MenuGroupService;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuGroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupRepository menuGroupRepository;
    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 뼈치킨;
    private MenuGroup 순살치킨;

    @BeforeEach
    void setUp() {
        뼈치킨 = new MenuGroup("뼈치킨");
        순살치킨 = new MenuGroup("순살치킨");
    }

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void 메뉴그룹_생성() {
        // given
        when(menuGroupRepository.save(뼈치킨)).thenReturn(뼈치킨);

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(뼈치킨);

        // then
        assertThat(savedMenuGroup).isEqualTo(뼈치킨);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void 메뉴그룹_목록_조회() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(뼈치킨, 순살치킨);
        when(menuGroupRepository.findAll()).thenReturn(menuGroups);

        // when
        List<MenuGroup> selectMenuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(selectMenuGroups).hasSize(menuGroups.size()),
                () -> assertThat(selectMenuGroups).containsExactly(뼈치킨, 순살치킨)
        );
    }
}
