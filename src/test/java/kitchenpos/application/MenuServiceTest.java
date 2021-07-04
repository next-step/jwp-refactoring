package kitchenpos.application;

import kitchenpos.dao.MenuDao;
import kitchenpos.dao.MenuGroupDao;
import kitchenpos.dao.MenuProductDao;
import kitchenpos.dao.ProductDao;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MenuServiceTest {
	@Autowired
	private MenuDao menuDao;

	@Autowired
	private MenuGroupDao menuGroupDao;

	@Autowired
	private MenuProductDao menuProductDao;
	
	@Autowired
	private ProductDao productDao;
	
	@Test
	public void 메뉴등록_성공(){
	    
	}
}