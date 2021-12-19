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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("이름을 통해 메뉴 그룹을 생성 할 수 있다.")
    @Test
    void create() {
        // given
        MenuGroup request = getCreateRequest("점심메뉴");
        MenuGroup expected = getMenuGroup(1L, "점심메뉴");
        given(menuGroupDao.save(request)).willReturn(expected);
        // when
        MenuGroup actual = menuGroupService.create(request);
        // then
        assertAll(
                () -> assertThat(actual.getId()).isEqualTo(expected.getId()),
                () -> assertThat(actual.getName()).isEqualTo(expected.getName())
        );
    }

    @DisplayName("목록을 조회 할 수 있다.")
    @Test
    void list() {
        // given
        final MenuGroup 점심메뉴 = getMenuGroup(1L, "점심메뉴");
        final MenuGroup 저녁메뉴 = getMenuGroup(2L, "저녁메뉴");
        final List<MenuGroup> expected = Arrays.asList(점심메뉴, 저녁메뉴);
        given(menuGroupDao.findAll()).willReturn(expected);
        // when
        List<MenuGroup> list = menuGroupService.list();
        // then
        assertThat(list).containsExactlyElementsOf(expected);
    }

    public static MenuGroup getMenuGroup(long id, String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        menuGroup.setId(id);
        return menuGroup;
    }

    private MenuGroup getCreateRequest(String name) {
        final MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName(name);
        return menuGroup;
    }
}