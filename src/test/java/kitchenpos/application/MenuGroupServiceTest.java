package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    MenuGroupDao menuGroupDao;

    @InjectMocks
    MenuGroupService menuGroupService;

    MenuGroup 양식;

    @BeforeEach
    void setUp() {
        양식 = new MenuGroup();
        양식.setName("양식");
    }

    @Test
    @DisplayName("메뉴 그룹 정상 등록 확인")
    public void create() {
        given(menuGroupDao.save(any(MenuGroup.class))).willReturn(양식);

        MenuGroup savedMenuGroup = menuGroupService.create(양식);
        assertThat(savedMenuGroup.getName()).isEqualTo(양식.getName());
    }

    @Test
    @DisplayName("전체 조회")
    public void list(){
        MenuGroup 분식 = new MenuGroup();
        분식.setName("분식");

        given(menuGroupDao.findAll()).willReturn(Arrays.asList(양식, 분식));

        List<MenuGroup> menuGroups = menuGroupService.list();

        assertThat(menuGroups.size()).isEqualTo(2);
    }
}