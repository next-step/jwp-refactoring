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
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
    @Mock
    private MenuGroupDao menuGroupDao;
    @InjectMocks
    private MenuGroupService menuGroupService;

    private MenuGroup 중식;
    private MenuGroup 분식;

    @BeforeEach
    void init() {
        중식 = new MenuGroup(1L,"중식");
        분식 = new MenuGroup(2L, "분식");
    }

    @DisplayName("메뉴그룹 등록 테스트")
    @Test
    void 메뉴그룹_생성() {
        //given
        MenuGroup 디저트 = new MenuGroup(3L, "디저트");
        when(menuGroupDao.save(any())).thenReturn(디저트);

        //when
        디저트 = menuGroupService.create(디저트);

        //then
        assertThat(디저트.getName()).isEqualTo("디저트");
        assertThat(디저트.getId()).isEqualTo(3L);
    }

    @DisplayName("메뉴그룹 목록 조회 테스트")
    @Test
    void 메뉴그룹_목록_조회() {
        //given
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(중식, 분식));

        //when
        List<MenuGroup> menuGroups = menuGroupService.list();

        //then
        assertThat(menuGroups).containsAll(Arrays.asList(중식, 분식));
    }
}
