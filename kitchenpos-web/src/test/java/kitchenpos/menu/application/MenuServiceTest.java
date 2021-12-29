package kitchenpos.menu.application;

import kitchenpos.menu.dto.MenuResponse;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static kitchenpos.menu.fixtures.MenuFixtures.양념치킨두마리메뉴;
import static kitchenpos.menu.fixtures.MenuFixtures.양념치킨두마리메뉴요청;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.BDDMockito.given;

/**
 * packageName : kitchenpos.application
 * fileName : MenuServiceTest
 * author : haedoang
 * date : 2021/12/17
 * description :
 */
@DisplayName("메뉴 통합 테스트")
@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {
    @Mock
    private MenuRepository menuRepository;

    @Mock
    private MenuValidator menuValidator;

    @InjectMocks
    private MenuService menuService;

    @Test
    @DisplayName("메뉴를 조회할 수 있다.")
    public void list() {
        // given
        given(menuRepository.findAllJoinFetch()).willReturn(Lists.newArrayList(양념치킨두마리메뉴()));

        // when
        List<MenuResponse> menus = menuService.list();

        // then
        assertThat(menus).hasSize(1);
    }

    @Test
    @DisplayName("메뉴를 등록할 수 있다.")
    public void create() {


        // when
        MenuResponse actual = menuService.create(양념치킨두마리메뉴요청());

        // then
        assertThat(actual.getMenuProducts()).hasSize(1);
    }
}
