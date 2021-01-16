package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.ProductDao;
import kitchenpos.domain.MenuGroup;
import kitchenpos.domain.Product;
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
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    private MenuGroup 일반메뉴;
    private MenuGroup 스페셜메뉴;

    @BeforeEach
    void setUp() {
        일반메뉴 = new MenuGroup();
        일반메뉴.setId(1L);
        일반메뉴.setName("일반메뉴");

        스페셜메뉴 = new MenuGroup();
        스페셜메뉴.setId(2L);
        스페셜메뉴.setName("스페셜메뉴");
    }

    @DisplayName("메뉴 그룹을 등록할 수 있다.")
    @Test
    void create() {
        //given
        given(menuGroupDao.save(any())).willReturn(일반메뉴);
        MenuGroup menuGroup = new MenuGroup();
        menuGroup.setName("일반메뉴");
        //when
        MenuGroup createMenuGroup = menuGroupService.create(menuGroup);
        //then
        assertThat(createMenuGroup.getId()).isNotNull();
        assertThat(createMenuGroup.getName()).isEqualTo("일반메뉴");
    }

    @DisplayName("메뉴 그룹 목록을 조회할 수 있다.")
    @Test
    void list() {
        //given
        given(menuGroupDao.findAll()).willReturn(Arrays.asList(일반메뉴, 스페셜메뉴));
        //when
        List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups.size()).isEqualTo(2);
        assertThat(menuGroups.get(0).getName()).isEqualTo("일반메뉴");
        assertThat(menuGroups.get(1).getName()).isEqualTo("스페셜메뉴");
    }
}