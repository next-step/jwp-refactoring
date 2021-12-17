package kitchenpos.menu;

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

import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

@DisplayName("메뉴 그룹 서비스 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuGroupServiceTest {
  @InjectMocks
  MenuGroupService menuGroupService;

  @Mock
  MenuGroupDao menuGroupDao;

  private MenuGroup 튀김종류;

  @BeforeEach
  void setUp() {
    튀김종류 = MenuFactory.ofMenuGroup("튀김종류");
  }

  @DisplayName("메뉴 그룹을 생성한다.")
  @Test
  void 메뉴_그룹_생성() {
    // given
    MenuGroup expected = MenuFactory.ofMenuGroup(1L, "튀김종류");
    given(menuGroupDao.save(튀김종류)).willReturn(expected);

    // when
    MenuGroup response = menuGroupService.create(튀김종류);

    // then
    assertAll(
            () -> assertThat(response.getId()).isEqualTo(expected.getId()),
            () -> assertThat(response.getName()).isEqualTo(튀김종류.getName())
    );
  }

  @DisplayName("메뉴 그룹 목록을 조회한다.")
  @Test
  void 메뉴_그룹_목록_조회() {
    // given
    MenuGroup 튀김종류_예상_결과 = MenuFactory.ofMenuGroup(1L, "튀김종류");
    MenuGroup 중식종류_예상_결과 = MenuFactory.ofMenuGroup(2L, "중식종류");
    given(menuGroupDao.findAll()).willReturn(Arrays.asList(튀김종류_예상_결과, 중식종류_예상_결과));

    // when
    List<MenuGroup> response = menuGroupService.list();

    // then
    assertAll(
            () -> assertThat(response.size()).isEqualTo(2),
            () -> assertThat(response).contains(튀김종류_예상_결과, 중식종류_예상_결과)
    );
  }
}
