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
import static org.mockito.Mockito.when;

@DisplayName("애플리케이션 테스트 보호 - 메뉴 그룹 서비스")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void create() {
        MenuGroup request = new MenuGroup();
        request.setName("메뉴그룹");

        MenuGroup expected = new MenuGroup();
        expected.setId(1L);
        expected.setName(request.getName());

        when(menuGroupDao.save(request)).thenReturn(expected);

        MenuGroup savedMenuGroup = menuGroupService.create(request);

        assertThat(savedMenuGroup).isEqualTo(expected);

    }

    @DisplayName("메뉴 그룹 목록 조회")
    @Test
    void list() {
        MenuGroup expected = new MenuGroup();
        expected.setId(1L);
        expected.setName("메뉴 그룹");

        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(expected));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups).containsExactly(expected);

    }

}
