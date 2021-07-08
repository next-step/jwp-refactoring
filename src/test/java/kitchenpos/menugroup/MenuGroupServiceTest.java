package kitchenpos.menugroup;

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

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 한마리메뉴;
    private MenuGroup 두마리메뉴;

    @BeforeEach
    void setUp() {
        한마리메뉴 = new MenuGroup();
        한마리메뉴.setId(1L);
        한마리메뉴.setName("한마리메뉴");

        두마리메뉴 = new MenuGroup();
        두마리메뉴.setId(2L);
        두마리메뉴.setName("두마리메뉴");
    }

    @DisplayName("메뉴 그룹을 등록한다")
    @Test
    void 메뉴그룹_등록() {
        //Given
        when(menuGroupDao.save(한마리메뉴)).thenReturn(한마리메뉴);

        //When
        MenuGroup 생성된_메뉴그룹 = menuGroupService.create(한마리메뉴);

        //Then
        assertThat(생성된_메뉴그룹.getId()).isNotNull();
        assertThat(생성된_메뉴그룹.getName()).isEqualTo(한마리메뉴.getName());
    }

    @DisplayName("메뉴 그룹 목록을 조회한다")
    @Test
    void 메뉴그룹_목록_조회() {
        //Given
        List<MenuGroup> 입력한_메뉴그룹_목록 = new ArrayList<>(Arrays.asList(한마리메뉴, 두마리메뉴));
        when(menuGroupDao.findAll()).thenReturn(입력한_메뉴그룹_목록);

        //When
        List<MenuGroup> 조회된_메뉴그룹_목록 = menuGroupService.list();

        //Then
        assertThat(조회된_메뉴그룹_목록).isNotNull()
                .hasSize(입력한_메뉴그룹_목록.size())
                .containsExactly(한마리메뉴, 두마리메뉴);
    }
}
