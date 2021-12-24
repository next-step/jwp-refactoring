package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
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

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴그룹 등록")
    @Test
    void 메뉴그룹_등록() {
        // given
        MenuGroup expected = new MenuGroup(1L, "추천메뉴");
        given(menuGroupDao.save(expected))
            .willReturn(expected);

        // when
        MenuGroup actual = menuGroupService.create(expected);

        // then
        assertThat(actual.getId()).isEqualTo(expected.getId());
        assertThat(actual.getName()).isEqualTo(expected.getName());
    }

    @DisplayName("메뉴그룹 조회")
    @Test
    void 메뉴그룹_목록_조회() {
        // given
        MenuGroup menuGroup1 = new MenuGroup(1L, "추천메뉴");
        MenuGroup menuGroup2 = new MenuGroup(2L, "A 세트메뉴");
        List<MenuGroup> expected = Arrays.asList(menuGroup1, menuGroup2);
        given(menuGroupDao.findAll())
            .willReturn(expected);

        // when
        List<MenuGroup> actual = menuGroupService.list();

        // then
        assertThat(actual).containsAll(expected);
    }

}
