package kitchenpos.application;

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

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        // given
        final MenuGroup request = createMenuGroupRequest("한마리메뉴");
        final MenuGroup expected = createMenuGroup(1L, "한마리메뉴");
        given(menuGroupDao.save(request)).willReturn(expected);

        // when
        final MenuGroup actual = menuGroupService.create(request);

        // then
        assertAll(
            () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
            () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        // given
        final List<MenuGroup> expected = Arrays.asList(
            createMenuGroup(1L, "한마리메뉴"),
            createMenuGroup(2L, "두마리메뉴")
        );
        given(menuGroupDao.findAll()).willReturn(expected);

        // when
        final List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).containsExactlyElementsOf(expected);
    }

    private MenuGroup createMenuGroupRequest(final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }

    private MenuGroup createMenuGroup(final Long id, final String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
