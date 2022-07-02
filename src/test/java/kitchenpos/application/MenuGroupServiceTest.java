package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.MenuGroupRepository;
import kitchenpos.dto.MenuGroupRequest;
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
class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;

    @Mock
    private MenuGroupRepository menuGroupRepository;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹을 생성한다.")
    @Test
    void create() {
        final String name = "치킨";
        MenuGroupRequest request = new MenuGroupRequest(name);
        MenuGroup 치킨 = new MenuGroup(name);
        given(menuGroupRepository.save(any(MenuGroup.class))).willReturn(치킨);

        MenuGroup result = menuGroupService.create(request);

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
