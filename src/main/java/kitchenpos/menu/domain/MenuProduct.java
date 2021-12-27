package kitchenpos.menu.domain;

import java.util.Objects;

import javax.persistence.AttributeOverride;
import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ForeignKey;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import kitchenpos.common.domain.PositiveNumber;
import kitchenpos.common.domain.Price;
import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;
import kitchenpos.product.domain.Product;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
	private Menu menu;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
	private Product product;

	@Embedded
	@AttributeOverride(name = "number", column = @Column(name = "quantity", nullable = false))
	private PositiveNumber quantity;

	protected MenuProduct() {
	}

	private MenuProduct(Long seq, Menu menu, Product product, PositiveNumber quantity) {
		this.seq = seq;
		this.menu = menu;
		this.product = product;
		this.quantity = quantity;
	}

	public static MenuProduct create(Product product, Long quantity) {
		validateCreate(product);
		return new MenuProduct(null, null, product, PositiveNumber.valueOf(quantity));
	}

	public static MenuProduct of(Long seq, Menu menu, Product product, Long quantity) {
		return new MenuProduct(seq, menu, product, PositiveNumber.valueOf(quantity));
	}

	private static void validateCreate(Product product) {
		if (Objects.isNull(product)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "MenuProduct 은 Product 이 필수입니다");
		}
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Price getTotalPrice() {
		return product.getPrice().multiply(quantity.toLong());
	}

	public Long getSeq() {
		return seq;
	}

	public Menu getMenu() {
		return menu;
	}

	public Product getProduct() {
		return product;
	}

	public PositiveNumber getQuantity() {
		return quantity;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o)
			return true;
		if (o == null || getClass() != o.getClass())
			return false;

		MenuProduct that = (MenuProduct)o;

		return seq.equals(that.seq);
	}

	@Override
	public int hashCode() {
		return seq.hashCode();
	}

}
