package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        final String name = "치킨";
        MenuGroup 치킨 = new MenuGroup(name);
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(치킨);

        MenuGroup result = menuGroupService.create(치킨);

        assertThat(result.getName()).isEqualTo(name);
    }

    @DisplayName("메뉴 그룹 목록을 조회한다.")
    @Test
    void list() {
        final String name = "치킨";
        MenuGroup 치킨 = new MenuGroup(name);
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(치킨));

        List<MenuGroup> result = menuGroupService.list();

        assertThat(result.size()).isEqualTo(1);
        assertThat(result.get(0).getName()).isEqualTo(name);
    }
}
