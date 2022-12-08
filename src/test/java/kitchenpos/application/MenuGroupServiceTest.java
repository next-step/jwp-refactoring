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
import static org.mockito.Mockito.when;

@DisplayName("MenuGroupService 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void createMenuGroup() {
        // given
        MenuGroup 한식 = new MenuGroup(1L, "한식");
        when(menuGroupDao.save(한식)).thenReturn(한식);

        // when
        MenuGroup result = menuGroupService.create(한식);

        // then
        assertAll(
                () -> assertThat(result.getId()).isEqualTo(한식.getId()),
                () -> assertThat(result.getName()).isEqualTo(한식.getName())
        );
    }

    @DisplayName("메뉴 그룹을 조회한다.")
    @Test
    void findAllMenuGroup() {
        // given
        MenuGroup 한식 = new MenuGroup(1L, "한식");
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(한식));

        // when
        List<MenuGroup> results = menuGroupService.list();

        // then
        assertAll(
                () -> assertThat(results).hasSize(1),
                () -> assertThat(results.get(0).getId()).isEqualTo(한식.getId()),
                () -> assertThat(results.get(0).getName()).isEqualTo(한식.getName())
        );
    }
}
