package kitchenpos.MenuGroup;

import kitchenpos.application.MenuGroupService;
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
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @InjectMocks
    MenuGroupService menuGroupService;

    @Mock
    MenuGroupDao menuGroupDao;

    private MenuGroup 두마리메뉴;
    private MenuGroup 한마리메뉴;

    @BeforeEach
    void setUp() {
        두마리메뉴 = MenuGroup.of((long)1, "두마리메뉴");
        한마리메뉴 = MenuGroup.of((long)2, "한마리메뉴");
    }

    @DisplayName("메뉴 그룹 등록")
    @Test
    void createMenuGroup() {
        // given
        when(menuGroupDao.save(두마리메뉴))
                .thenReturn(두마리메뉴);

        // when
        MenuGroup result = menuGroupService.create(두마리메뉴);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(두마리메뉴.getId()),
                () -> assertThat(result.getName()).isEqualTo(두마리메뉴.getName())
        );
    }

    @DisplayName("메뉴 그룹 전체 조회")
    @Test
    void findAllMenuGroups() {
        // given
        when(menuGroupDao.findAll())
                .thenReturn(Arrays.asList(두마리메뉴, 한마리메뉴));

        // when
        List<MenuGroup> list = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(list).hasSize(2),
                () -> assertThat(list).containsExactly(두마리메뉴, 한마리메뉴)
        );
    }
}
