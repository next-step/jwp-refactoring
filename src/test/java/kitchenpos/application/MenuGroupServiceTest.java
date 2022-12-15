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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
@DisplayName("메뉴그룹")
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroup 메뉴그룹1;
    private MenuGroup 메뉴그룹2;
    private MenuGroup 메뉴그룹3;

    @BeforeEach
    void setUp() {
        메뉴그룹1 = 메뉴그룹_생성(1L, "menuGroup1");
        메뉴그룹2 = 메뉴그룹_생성(2L, "menuGroup2");
        메뉴그룹3 = 메뉴그룹_생성(3L, "menuGroup3");
    }

    @Test
    @DisplayName("전체 메뉴 그룹을 조회할 수 있다.")
    void menuGroupTest1() {
        List<MenuGroup> 메뉴그룹들 = 메뉴그룹들_생성();

        given(menuGroupDao.findAll()).willReturn(메뉴그룹들);

        List<MenuGroup> 조회된_메뉴그룹들 = menuGroupService.list();
        assertThat(조회된_메뉴그룹들.size()).isEqualTo(메뉴그룹들.size());
    }

    @Test
    @DisplayName("새로운 메뉴 그룹을 추가할 수 있다.")
    void menuGroupTest2() {
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(메뉴그룹1);

        MenuGroup 생성된_메뉴그룹 = menuGroupService.create(메뉴그룹1);
        assertThat(생성된_메뉴그룹.getName()).isEqualTo(메뉴그룹1.getName());
    }

    public static MenuGroup 메뉴그룹_생성(Long id, String name) {
        return MenuGroup.of(id, name);
    }

    private List<MenuGroup> 메뉴그룹들_생성() {
        return Arrays.asList(메뉴그룹1, 메뉴그룹2, 메뉴그룹3);
    }

}