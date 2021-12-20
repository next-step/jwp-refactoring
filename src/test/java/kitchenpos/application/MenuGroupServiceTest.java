package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 등록할 수 있다")
    @Test
    void 메뉴그룹_등록() {
        // given
        MenuGroup 메뉴그룹 = new MenuGroup();
        메뉴그룹.setId(1L);
        메뉴그룹.setName("한식");
        given(menuGroupDao.save(메뉴그룹)).willReturn(메뉴그룹);

        // when
        MenuGroup savedMenuGroup = menuGroupService.create(메뉴그룹);

        // then
        assertThat(savedMenuGroup).isEqualTo(메뉴그룹);

    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다")
    @Test
    void 메뉴그룹_목록_조회() {
        // given
        MenuGroup 첫번째_메뉴그룹 = new MenuGroup();
        첫번째_메뉴그룹.setId(1L);
        첫번째_메뉴그룹.setName("한식");
        
        MenuGroup 두번째_메뉴그룹 = new MenuGroup();
        두번째_메뉴그룹.setId(2L);
        두번째_메뉴그룹.setName("양식");
        
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(첫번째_메뉴그룹, 두번째_메뉴그룹));

        // when
        List<MenuGroup> menuGroups = menuGroupService.list();

        // then
        assertThat(menuGroups).containsExactly(첫번째_메뉴그룹, 두번째_메뉴그룹);
    }

}
