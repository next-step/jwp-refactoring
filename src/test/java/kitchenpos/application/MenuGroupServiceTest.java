package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
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
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    private MenuGroup 커피류;
    private MenuGroup 차류;

    @BeforeEach
    void setUp() {
        커피류 = new MenuGroup(1L, "커피류");
        차류 = new MenuGroup(2L, "차류");
    }

    @Test
    @DisplayName("메뉴 그룹을 등록한다.")
    void 메뉴_그룹_등록() {
        // given
        given(menuGroupDao.save(커피류)).willReturn(커피류);

        // when
        MenuGroup saveMenuGroup = menuGroupService.create(커피류);

        // then
        assertThat(saveMenuGroup.getId()).isEqualTo(커피류.getId());
        assertThat(saveMenuGroup.getName()).isEqualTo(커피류.getName());
    }

    @Test
    @DisplayName("메뉴 그룹 목록을 조회한다.")
    void 메뉴_그룹_목록_조회() {
        // given
        List<MenuGroup> menuGroupList = Arrays.asList(커피류, 차류);
        given(menuGroupDao.findAll()).willReturn(menuGroupList);

        // when
        List<MenuGroup> searchMenuGroup = menuGroupService.list();

        // then
        assertThat(searchMenuGroup).containsExactly(커피류, 차류);
    }
}
