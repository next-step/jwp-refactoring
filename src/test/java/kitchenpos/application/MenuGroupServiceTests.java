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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.lenient;

@DisplayName("메뉴그룹 기능 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTests {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @Test
    public void 메뉴그룹_생성() {
        MenuGroup 기본메뉴그룹 = new MenuGroup(1L, "기본메뉴그룹");
        lenient().when(menuGroupDao.save(기본메뉴그룹))
                .thenReturn(기본메뉴그룹);
        assertThat(menuGroupService.create(기본메뉴그룹))
                .isNotNull()
                .isInstanceOf(MenuGroup.class)
                .isEqualTo(기본메뉴그룹);
    }

    @Test
    public void 메뉴그룹_조회() {
        MenuGroup 기본메뉴그룹 = new MenuGroup(1L, "기본메뉴그룹");
        menuGroupService.create(기본메뉴그룹);
        MenuGroup 추가메뉴그룹 = new MenuGroup(2L, "추가메뉴그룹");
        menuGroupService.create(추가메뉴그룹);

        lenient().when(menuGroupDao.findAll())
                .thenReturn(Arrays.asList(기본메뉴그룹, 추가메뉴그룹));
        assertThat(menuGroupService.list())
                .hasSize(2)
                .contains(기본메뉴그룹, 추가메뉴그룹);
    }

}
