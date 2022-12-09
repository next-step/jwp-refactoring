package kitchenpos.application;

import static kitchenpos.domain.MenuGroupTestFixture.menuGroup;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 비즈니스 로직 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    @DisplayName("메뉴 그룹을 등록한다")
    void createMenuGroup() {
        // given
        MenuGroup 면류 = menuGroup(1L, "면류");
        given(menuGroupDao.save(면류)).willReturn(면류);

        // when
        MenuGroup actual = menuGroupService.create(면류);

        // then
        assertAll(
                () -> assertThat(actual).isNotNull(),
                () -> assertThat(actual).isInstanceOf(MenuGroup.class)
        );
    }

    @Test
    @DisplayName("메뉴 그룹을 조회하면 메뉴 그룹 목록을 반환한다.")
    void findMenuGroups() {
        MenuGroup 면류 = menuGroup(1L, "면류");
        MenuGroup 밥류 = menuGroup(2L, "밥류");
        List<MenuGroup> menuGroups = Arrays.asList(면류, 밥류);
        given(menuGroupDao.findAll()).willReturn(menuGroups);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(actual).hasSize(2),
                () -> assertThat(actual).containsExactly(면류, 밥류)
        );
    }
}
