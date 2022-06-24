package kitchenpos.application;

import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Mock
    private MenuGroupDao menuGroupDao;

    private MenuGroup 추천_메뉴;
    private MenuGroup 강력_추천_메뉴;

    @BeforeEach
    void setUp() {
        추천_메뉴 = new MenuGroup();
        추천_메뉴.setName("추천 메뉴");

        강력_추천_메뉴 = new MenuGroup();
        강력_추천_메뉴.setName("강력_추천_메뉴");
    }

    @Test
    @DisplayName("메뉴 그룹 등록 테스트")
    void create() {
        when(menuGroupDao.save(추천_메뉴)).thenReturn(추천_메뉴);

        MenuGroup 추천_메뉴_등록_결과 = menuGroupService.create(추천_메뉴);

        Assertions.assertThat(추천_메뉴).isEqualTo(추천_메뉴_등록_결과);
    }

    @Test
    @DisplayName("메뉴 그룹 조회 테스트")
    void findAll() {
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(추천_메뉴, 강력_추천_메뉴));

        List<MenuGroup> 추천_메뉴_조회_결과 = menuGroupDao.findAll();

        assertAll(
                () -> Assertions.assertThat(추천_메뉴_조회_결과).hasSize(2),
                () -> Assertions.assertThat(추천_메뉴_조회_결과).containsExactly(추천_메뉴, 강력_추천_메뉴)
        );
    }
}
