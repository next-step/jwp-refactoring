package kitchenpos.common;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@MappedSuperclass
public abstract class BaseSeqEntity {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long seq;

	protected BaseSeqEntity() {
	}

	protected BaseSeqEntity(Long seq) {
		this.seq = seq;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (!(o instanceof BaseSeqEntity)) return false;
		BaseSeqEntity that = (BaseSeqEntity) o;
		return Objects.equals(seq, that.seq);
	}

	@Override
	public int hashCode() {
		return Objects.hash(seq);
	}

	public Long getSeq() {
		return seq;
	}
}
