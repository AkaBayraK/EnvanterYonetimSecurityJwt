package com.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
@Entity
@Table(name = "ROLE_TBL", schema = "envanterDB")
public class RoleEntity extends BaseEntity {
	

	private static final long serialVersionUID = 5684434332210380012L;
	
	public static final String ROLE_NAME_ADMIN	=	"ADMIN";
	
	//Criteria için gerekli
	public static final String ROLE_COLUMN_NAME_NAME	=	"name";
	
	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	
    @Column(name = "NAME")
    private String name;
 
	@SuppressWarnings("unchecked")
	public Class<?> getEntityClass() {
		return RoleEntity.class;
	}
	
    public RoleEntity() {
	}
    
	@Override
	public int hashCode() {
		return Objects.hash(id, name);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleEntity other = (RoleEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(name, other.name);
	}

	@Override
	public String toString() {
		return "RoleEntity [id=" + id + ", name=" + name + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
}
