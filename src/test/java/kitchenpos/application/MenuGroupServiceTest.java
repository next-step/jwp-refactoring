package kitchenpos.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.domain.MenuGroup;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@DisplayName("메뉴 그룹 Business Object 테스트")
@ExtendWith(MockitoExtension.class)
class MenuGroupServiceTest {

    @Mock
    private MenuGroupDao menuGroupDao;

    @InjectMocks
    private MenuGroupService menuGroupService;

    @DisplayName("메뉴 그룹 생성")
    @Test
    void 메뉴_그룹_생성() {
        MenuGroup 세트 = new MenuGroup(1L, "세트");
        when(menuGroupDao.save(세트)).thenReturn(세트);

        MenuGroup 생성된_메뉴_그룹 = menuGroupService.create(세트);

        assertAll(
                () -> assertThat(생성된_메뉴_그룹.getId()).isEqualTo(1L),
                () -> assertThat(생성된_메뉴_그룹.getName()).isEqualTo("세트")
        );
    }

    @DisplayName("비어있는 이름으로 메뉴 그룹 생성 요청 시 예외처리")
    @Test
    void 비어있는_이름_메뉴_그룹_생성() {
        MenuGroup 빈_이름_메뉴_그룹 = new MenuGroup(1L, "");

        assertThatThrownBy(
                () -> menuGroupService.create(빈_이름_메뉴_그룹)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    @DisplayName("메뉴 그룹 조회")
    @Test
    void 메뉴_그룹_조회() {
        MenuGroup 세트 = new MenuGroup(1L, "세트");
        MenuGroup 단품 = new MenuGroup(2L, "단품");
        when(menuGroupDao.findAll()).thenReturn(Arrays.asList(세트, 단품));
        List<MenuGroup> 조회된_메뉴_그룹_목록 = menuGroupService.list();

        assertThat(조회된_메뉴_그룹_목록).containsAll(Arrays.asList(세트, 단품));
    }
}