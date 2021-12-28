package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

@DisplayName("주문 테이블들 도메인 테스트")
public class OrderTablesTest {

	private OrderTable 테이블;
	private OrderTable 빈_테이블;

	@BeforeEach
	void setup() {
		// given
		Product 후라이드치킨 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(17_000));
		MenuGroup 추천메뉴 = MenuGroup.of(1L, "추천메뉴");
		MenuProduct 메뉴_상품 = MenuProduct.of(후라이드치킨, 2L);

		Menu 후라이드둘세트 = Menu.of(1L, "후라이드 둘 세트", BigDecimal.valueOf(32_000), 추천메뉴);
		후라이드둘세트.addMenuProducts(Collections.singletonList(메뉴_상품));
		테이블 = OrderTable.of(1L, 4, false);
		빈_테이블 = OrderTable.of(2L, 4, true);
	}

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(OrderTables.of(Arrays.asList(테이블, 빈_테이블)))
			.isEqualTo(OrderTables.of(Arrays.asList(테이블, 빈_테이블)));
	}

	@DisplayName("테이블 그룹을 해제한다")
	@Test
	void unGroupTest() {
		//
		TableGroup 테이블_그룹 = TableGroup.of(1L, Arrays.asList(테이블, 테이블));
		OrderTable 속한_테이블 = OrderTable.of(1L, 테이블_그룹, NumberOfGuests.of(4), false);
		OrderTables orderTables = OrderTables.of(Collections.singletonList(속한_테이블));

		// when
		orderTables.unGroup();

		// then
		assertThat(속한_테이블.getTableGroup()).isNull();
	}
}
