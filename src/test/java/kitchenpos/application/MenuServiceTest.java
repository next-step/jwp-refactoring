package kitchenpos.application;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.domain.Menu;
import kitchenpos.domain.MenuTest;

@ExtendWith(MockitoExtension.class)
public class MenuServiceTest {

	@InjectMocks
	private MenuService menuService;

	@Mock
	private MenuDao menuDao;

	@Mock
	private MenuProductDao menuProductDao;

	@DisplayName("메뉴 목록을 조회한다")
	@Test
	void listTest() {
		// given
		List<Menu> persist = new ArrayList<>();
		persist.add(MenuTest.후라이드_양념_세트);
		given(menuDao.findAll()).willReturn(persist);
		given(menuProductDao.findAllByMenuId(any()))
			.willReturn(MenuTest.후라이드_양념_세트.getMenuProducts());

		// when
		List<Menu> result = menuService.list();

		// then
		assertThat(result).containsAll(persist);
	}

	@DisplayName("메뉴를 등록한다")
	@Test
	void createTest() {
		// given
		Menu request = new Menu();
		request.setName("후라이드+후라이드");
		// when

		// then

	}

	@DisplayName("메뉴 가격이 0 미만이면 등록할 수 없다")
	@Test
	void createTest2() {

	}

	@DisplayName("메뉴 그룹을 지정하지 않으면 등록할 수 없다")
	@Test
	void createTest3() {

	}

	@DisplayName("메뉴의 상품이 모두 등록되어 있어야 등록할 수 있다")
	@Test
	void createTest4() {

	}

	@DisplayName("메뉴 가격이 메뉴 재료 원가보다 비싸야 한다")
	@Test
	void createTest5() {

	}

}
