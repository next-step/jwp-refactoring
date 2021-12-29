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

import kitchenpos.common.domain.Quantity;
import kitchenpos.exception.AppException;
import kitchenpos.exception.ErrorCode;

@Entity
@Table(name = "menu_product")
public class MenuProduct {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "menu_id", foreignKey = @ForeignKey(name = "fk_menu_product_menu"), nullable = false)
	private Menu menu;

	@JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_menu_product_product"), nullable = false)
	private Long productId;

	@Embedded
	@AttributeOverride(name = "quantity", column = @Column(name = "quantity", nullable = false))
	private Quantity quantity;

	protected MenuProduct() {
	}

	private MenuProduct(Long seq, Menu menu, Long productId, Quantity quantity) {
		this.seq = seq;
		this.menu = menu;
		this.productId = productId;
		this.quantity = quantity;
	}

	public static MenuProduct create(Long productId, Long quantity) {
		validateCreate(productId);
		return new MenuProduct(null, null, productId, Quantity.valueOf(quantity));
	}

	public static MenuProduct of(Long seq, Menu menu, Long productId, Long quantity) {
		return new MenuProduct(seq, menu, productId, Quantity.valueOf(quantity));
	}

	private static void validateCreate(Long productId) {
		if (Objects.isNull(productId)) {
			throw new AppException(ErrorCode.WRONG_INPUT, "MenuProduct 은 ProductId 가 필수입니다");
		}
	}

	public static MenuProduct of(Long productId, Long quantity) {
		return new MenuProduct(null, null, productId, Quantity.valueOf(quantity));
	}

	public void setMenu(Menu menu) {
		this.menu = menu;
	}

	public Long getSeq() {
		return seq;
	}

	public Menu getMenu() {
		return menu;
	}

	public Long getProductId() {
		return productId;
	}

	public Quantity getQuantity() {
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
