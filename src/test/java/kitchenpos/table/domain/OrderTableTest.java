package kitchenpos.table.domain;

import static org.assertj.core.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.product.domain.Product;

public class OrderTableTest {

	private OrderTable 테이블;
	private OrderTable 빈_테이블;

	@BeforeEach
	void setup() {
		// given
		Product 후라이드치킨 = Product.of(1L, "후라이드치킨", BigDecimal.valueOf(17_000));
		MenuGroup 추천메뉴 = MenuGroup.of(1L, "추천메뉴");
		MenuProduct 메뉴_상품 = MenuProduct.of(후라이드치킨.getId(), 2L);

		Menu 후라이드둘세트 = Menu.of(1L, "후라이드 둘 세트", BigDecimal.valueOf(32_000), 추천메뉴);
		후라이드둘세트.addMenuProducts(Collections.singletonList(메뉴_상품));
		테이블 = OrderTable.of(1L, 4, false);
		빈_테이블 = OrderTable.of(2L, 4, true);
	}

	@DisplayName("생성 테스트")
	@Test
	void createTest() {
		assertThat(OrderTable.of(1L, 3, false))
			.isEqualTo(OrderTable.of(1L, 3, false));
	}

	@DisplayName("상태 변경을 할 수 있다")
	@Test
	void changeEmptyStatusTest() {
		// when
		테이블.changeEmptyStatus(true);

		// then
		assertThat(테이블.isEmpty()).isTrue();
	}

	@DisplayName("상태 변경 시, 테이블 그룹에 속해 있지 않아야 한다")
	@Test
	void changeEmptyStatusTest2() {
		// given
		TableGroup 테이블_그룹 = TableGroup.of(1L, Arrays.asList(테이블, 테이블));
		OrderTable 속한_테이블 = OrderTable.of(1L, 테이블_그룹, NumberOfGuests.of(4), false);

		// when, then
		assertThatThrownBy(() -> 속한_테이블.changeEmptyStatus(true))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("손님 인원을 변경을 할 수 있다")
	@Test
	void changeNumberOfGuestsTest() {
		// when
		테이블.changeNumberOfGuests(10);

		// then
		assertThat(테이블.getNumberOfGuests().toInt()).isEqualTo(10);
	}

	@DisplayName("빈 테이블의 인원을 변경할 수 없다")
	@Test
	void changeNumberOfGuestsTest2() {
		// when, then
		assertThatThrownBy(() -> 빈_테이블.changeNumberOfGuests(10))
			.isInstanceOf(AppException.class)
			.hasMessage(ErrorCode.WRONG_INPUT.getMessage());
	}

	@DisplayName("테이블 그룹을 해제할 수 있다")
	@Test
	void unGroupTest() {
		// given
		TableGroup 테이블_그룹 = TableGroup.of(1L, Arrays.asList(테이블, 테이블));
		OrderTable 속한_테이블 = OrderTable.of(1L, 테이블_그룹, NumberOfGuests.of(4), false);

		// when, then
		속한_테이블.unGroup();

		// then
		assertThat(속한_테이블.getTableGroup()).isNull();
	}
}
