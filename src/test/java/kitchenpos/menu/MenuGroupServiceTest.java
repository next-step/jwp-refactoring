package kitchenpos.menu;

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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹 관련 기능")
public class MenuGroupServiceTest {
    @Mock
    MenuGroupRepository menuGroupRepository;

    @InjectMocks
    MenuGroupService menuGroupService;

    private MenuGroup menuGroup;

    @BeforeEach
    void setUp() {
        menuGroup = 메뉴_그룹_등록("짜장면 세트");
    }

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    void createMenuGroup() {
        // given
        given(menuGroupRepository.save(any())).willReturn(menuGroup);

        // given
        MenuGroup createMenuGroup = menuGroupService.create(menuGroup);

        // then
        assertThat(createMenuGroup).isNotNull();
    }

    @Test
    @DisplayName("메뉴 그룹을 조회한다.")
    void getMenuGroup() {
        // given
        given(menuGroupRepository.findAll()).willReturn(Arrays.asList(menuGroup));

        // given
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).isNotNull();
    }

    public static MenuGroup 메뉴_그룹_등록(String name) {
        return new MenuGroup(name);
    }
}
