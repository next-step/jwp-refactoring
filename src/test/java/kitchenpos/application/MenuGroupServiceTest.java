package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록한다.")
    @Test
    void SaveMenuGroup() {
        final MenuGroup expected = new MenuGroup(1L, "메뉴그룹");
        given(menuGroupDao.save(any())).willReturn(expected);

        final MenuGroup actual = menuGroupService.create(expected);

        assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("등록한 메뉴들을 조회한다.")
    @Test
    void findMenuGroup() {
        final MenuGroup menuGroup1 = new MenuGroup(1L, "메뉴그룹1");
        final MenuGroup menuGroup2 = new MenuGroup(2L, "메뉴그룹2");
        final List<MenuGroup> menuGroups = Arrays.asList(menuGroup1, menuGroup2);
        given(menuGroupDao.findAll()).willReturn(menuGroups);

        final List<MenuGroup> actual = menuGroupService.list();

        assertThat(actual).hasSize(2);
    }
}
