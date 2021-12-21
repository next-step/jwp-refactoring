package kitchenpos.dao;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.sql.DataSource;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseBuilder;
import org.springframework.jdbc.datasource.embedded.EmbeddedDatabaseType;

import kitchenpos.domain.OrderLineItem;

class JdbcTemplateOrderLineItemDaoTest {


	private static DataSource dataSource = new EmbeddedDatabaseBuilder().setType(EmbeddedDatabaseType.H2)
		.addScript("classpath:db/migration/V1__Initialize_project_tables.sql")
		.addScript("classpath:db/migration/V2__Insert_default_data.sql")
		.build();

	private static JdbcTemplateOrderLineItemDao jdbcTemplateOrderLineItemDao = new JdbcTemplateOrderLineItemDao(dataSource);

	@Test
	@DisplayName("주문아이템 저장 테스트")
	public void saveOrderLineItemTest() {
		//given
		OrderLineItem orderLineItem = new OrderLineItem(null, 1L, 1L, 1L);

		//when
		OrderLineItem save = jdbcTemplateOrderLineItemDao.save(orderLineItem);

		//then
		assertThat(save).isNotNull();
		assertThat(save.getSeq()).isEqualTo(4L);
	}

	@Test
	@DisplayName("주문아이템 목록 조회 테스트")
	public void findAllOrderLineItemTest() {
		//given

		//when
		List<OrderLineItem> orderLineItems = jdbcTemplateOrderLineItemDao.findAll();

		//then
		assertThat(orderLineItems).hasSizeGreaterThanOrEqualTo(3);
	}

	@Test
	@DisplayName("주문아이템 id로 조회 테스트")
	public void findByIdOrderLineItemTest() {
		//given

		//when
		OrderLineItem orderLineItem = jdbcTemplateOrderLineItemDao.findById(1L).orElse(new OrderLineItem());

		//then
		assertThat(orderLineItem.getQuantity()).isEqualTo(1);
	}

	@Test
	@DisplayName("없는 주문아이템 id로 조회 테스트")
	public void findByIdMenuFailTest() {
		//given

		//when
		OrderLineItem orderLineItem = jdbcTemplateOrderLineItemDao.findById(9L).orElse(new OrderLineItem());

		//then
		assertThat(orderLineItem.getSeq()).isNull();
	}

	@Test
	@DisplayName("주문 Id로 주문아이템 조회")
	public void findAllByOrderIdTest() {
		//given

		//when
		List<OrderLineItem> orderLineItems = jdbcTemplateOrderLineItemDao.findAllByOrderId(2L);

		//then
		assertThat(orderLineItems).hasSize(1);
	}
}
