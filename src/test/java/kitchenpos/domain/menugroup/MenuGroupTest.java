package kitchenpos.domain.menugroup;

import kitchenpos.application.MenuGroupService;
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
import static org.assertj.core.api.Assertions.assertThatIllegalArgumentException;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴 그룹 관련 기능")
class MenuGroupTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    @Test
    @DisplayName("메뉴 그룹 이름이 비어있을 경우 예외가 발생한다.")
    void emptyMenuGroupName() {
        assertThatIllegalArgumentException().isThrownBy(() -> {
            menuGroupService.create(new MenuGroup(" "));
        });
    }

    @Test
    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    void createMenuGroup() {
        // given
        when(menuGroupDao.save(any())).thenReturn(new MenuGroup(1L, "추천메뉴"));

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(new MenuGroup("추천메뉴"));

        // then
        assertThat(savedMenuGroup.getId()).isEqualTo(1L);
        assertThat(savedMenuGroup.getName()).isEqualTo("추천메뉴");
    }

    @Test
    @DisplayName("메뉴 그룹을 조회할 수 있다.")
    void findMenuGroup() {
        // given
        List<MenuGroup> menuGroups = Arrays.asList(new MenuGroup(1L, "추천메뉴1"),
                new MenuGroup(2L, "추천메뉴2"));
        when(menuGroupDao.findAll()).thenReturn(menuGroups);

        // when
        List<MenuGroup> findByMenuGroups = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(findByMenuGroups.size()).isEqualTo(2),
                () -> assertThat(findByMenuGroups).extracting("id").contains(1L,2L)
        );
    }
}
