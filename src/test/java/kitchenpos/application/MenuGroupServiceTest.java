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
public class MenuGroupServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void 메뉴_그룹_생성_성공() {
        // given
        MenuGroup 양식 = 메뉴_그룹_생성(1L, "양식");
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(양식);

        // when
        MenuGroup saved = menuGroupService.create(양식);

        // then
        assertThat(saved).isNotNull();
        assertThat(saved.getName()).isEqualTo(양식.getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void 메뉴_그룹_목록_조회() {
        // given
        MenuGroup 양식 = 메뉴_그룹_생성(1L, "양식");
        MenuGroup 한식 = 메뉴_그룹_생성(2L, "한식");
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(양식, 한식));

        // when
        List<MenuGroup> 메뉴_그룹_목록 = menuGroupService.list();

        // then
        assertThat(메뉴_그룹_목록).containsExactly(양식, 한식);
    }

    public static MenuGroup 메뉴_그룹_생성(Long id, String name) {
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setId(id);
        menuGroup.setName(name);
        return menuGroup;
    }
}
