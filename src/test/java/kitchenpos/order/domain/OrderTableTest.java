package kitchenpos.order.domain;

import kitchenpos.common.entity.NumberOfGuests;
import kitchenpos.common.entity.Quantity;
import kitchenpos.menu.domain.Menu;
import kitchenpos.menu.domain.MenuGroup;
import kitchenpos.menu.domain.MenuProduct;
import kitchenpos.menu.domain.Product;
import kitchenpos.order.application.TableValidationException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.Arrays;
import java.util.Collections;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;

class OrderTableTest {

	private OrderTable orderTable;
	private TableGroup tableGroup;
	private Menu 짜장면;
	private Menu 짬뽕;
	private OrderItem orderItem_짬뽕;
	private OrderItem orderItem_짜장면;

	@BeforeEach
	void setUp() {
		orderTable = new OrderTable(20, true);
		tableGroup = mock(TableGroup.class);
		given(tableGroup.getId()).willReturn(50L);
		MenuGroup 중식 = new MenuGroup("중식");

		짜장면 = new Menu("짜장면", 7000, 중식);
		MenuProduct menuProduct1 = new MenuProduct(new Product("짜장면", 7000), 1);
		짜장면.addMenuProducts(Collections.singletonList(menuProduct1));

		짬뽕 = new Menu("짬뽕", 6000, 중식);
		MenuProduct menuProduct2 = new MenuProduct(new Product("짬뽕", 6000), 1);
		짬뽕.addMenuProducts(Collections.singletonList(menuProduct2));

		orderItem_짜장면 = OrderItem.of(짜장면, new Quantity(77));
		orderItem_짬뽕 = OrderItem.of(짬뽕, new Quantity(99));
	}

	@DisplayName("주문테이블의 공석 상태를 변경한다.")
	@ParameterizedTest
	@ValueSource(booleans = {true, false})
	void changeEmpty(boolean isEmpty) {
		// when
		orderTable.changeEmpty(isEmpty);

		// then
		assertThat(orderTable.isEmpty()).isEqualTo(isEmpty);
	}

	@DisplayName("단체지정된 주문테이블의 공석상태를 변경하려 할 때 예외 발생.")
	@Test
	void changeEmpty_ExceptionAlreadyGroup() {
		orderTable.putIntoGroup(tableGroup.getId());

		assertThatThrownBy(() -> orderTable.changeEmpty(true))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_CANNOT_CHANGE_EMPTY_ALREADY_GROUP);
	}

	@DisplayName("진행중인 주문이 있는 주문테이블의 공석상태를 변경하려 할 때 예외 발생.")
	@Test
	void changeEmpty_ExceptionWhileOngoingOrder() {
		// given: 테이블에 주문 넣기
		// 주문을 넣기 위한 사전 설정
		orderTable.changeEmpty(false);
		orderTable.order(Arrays.asList(orderItem_짜장면, orderItem_짬뽕));

		// when then
		assertThatThrownBy(() -> orderTable.changeEmpty(true))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_CANNOT_CHANGE_EMPTY_ONGOING_ORDER);
	}

	@DisplayName("주문테이블의 인원수를 변경한다.")
	@ParameterizedTest
	@ValueSource(ints = {0, 1, 99})
	void changeNumberOfGuests(int numberOfGuests) {
		orderTable.changeEmpty(false);

		orderTable.changeNumberOfGuests(numberOfGuests);

		assertThat(orderTable.getNumberOfGuests()).isEqualTo(new NumberOfGuests(numberOfGuests));
	}

	@DisplayName("공석상태인 주문테이블의 인원수를 변경하려 할 때 예외 발생.")
	@Test
	void changeNumberOfGuests_ExceptionNotEmpty() {
		orderTable.changeEmpty(true);

		assertThatThrownBy(() -> orderTable.changeNumberOfGuests(5))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_CANNOT_CHANGE_GUEST_WHILE_EMPTY);
	}

	@DisplayName("주문테이블을 테이블 그룹에 속하게 한다.")
	@Test
	void putIntoGroup() {
		// when
		orderTable.putIntoGroup(tableGroup.getId());

		// then
		assertThat(orderTable.isEmpty()).isFalse();
		assertThat(orderTable.getTableGroupId()).isEqualTo(tableGroup.getId());
	}

	@DisplayName("주문테이블 단체지정시 null 을 입력하면 예외 발생.")
	@Test
	void putIntoGroup_ExceptionGroupNull() {
		assertThatThrownBy(() -> orderTable.putIntoGroup(null))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_TABLE_GROUP_NOT_NULL);
	}

	@DisplayName("이미 테이블이 단체지정 되있을때 다른 그룹에 속하게 하면 예외 발생.")
	@Test
	void putIntoGroup_ExceptionAlreadyGroup() {
		TableGroup beforeTableGroup = mock(TableGroup.class);
		given(beforeTableGroup.getId()).willReturn(55L);
		orderTable.putIntoGroup(beforeTableGroup.getId());

		assertThatThrownBy(() -> orderTable.putIntoGroup(tableGroup.getId()))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_ORDER_TABLE_ALREADY_GROUP);
	}

	@DisplayName("테이블이 비어있지 않을때 단체지정시 예외 발생.")
	@Test
	void putIntoGroup_ExceptionNotEmpty() {
		orderTable.changeEmpty(false);

		assertThatThrownBy(() -> orderTable.putIntoGroup(tableGroup.getId()))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_ORDER_TABLE_EMPTY);
	}

	@DisplayName("테이블의 단체지정을 해제한다.")
	@Test
	void ungroup() {
		orderTable.putIntoGroup(tableGroup.getId());

		orderTable.ungroup();

		assertThat(orderTable.getTableGroupId()).isNull();
	}

	@DisplayName("단체지정을 해제할 때 진행중인 주문이 있을경우 예외 발생.")
	@Test
	void ungroup_OngoingOrder() {
		// given: 그룹화, 테이블에 주문 넣기
		orderTable.putIntoGroup(tableGroup.getId());
		orderTable.order(Arrays.asList(orderItem_짜장면, orderItem_짬뽕));

		assertThatThrownBy(() -> orderTable.ungroup())
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_ORDER_TABLE_ONGOING);
	}

	@DisplayName("주문테이블에서 주문을 한다.")
	@Test
	void order() {
		// given
		orderTable.changeEmpty(false);

		// when
		Order order = orderTable.order(Arrays.asList(orderItem_짬뽕, orderItem_짜장면));

		// then
		assertThat(order).isNotNull();
		assertThat(order.getOrderTable()).isEqualTo(orderTable);
	}

	@DisplayName("주문테이블이 공석일때 주문을 할 경우 예외 발생.")
	@Test
	void order_ExceptionEmpty() {
		orderTable.changeEmpty(true);

		assertThatThrownBy(() -> orderTable.order(Arrays.asList(orderItem_짬뽕, orderItem_짜장면)))
				.isInstanceOf(TableValidationException.class)
				.hasMessageMatching(OrderTable.MSG_CANNOT_ADD_ORDER_TABLE_EMPTY);
	}
}
