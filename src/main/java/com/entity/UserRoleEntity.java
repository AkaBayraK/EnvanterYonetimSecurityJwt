package com.entity;

import java.util.Objects;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Data
@Table(name = "USER_ROLE_TBL", schema = "envanterDB")
public class UserRoleEntity extends BaseEntity {
	

	private static final long serialVersionUID = 9118752422768473121L;

	@Id
	@Column(name = "ID")
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	
    @Column(name = "USER_ID")
    private Long userId;

	@ManyToOne(targetEntity=RoleEntity.class,fetch=FetchType.EAGER)
	@JoinColumn(name="ROLE_ID",referencedColumnName="ID", nullable = false)
	private RoleEntity role;
 
	@SuppressWarnings("unchecked")
	public Class<?> getEntityClass() {
		return UserRoleEntity.class;
	}
	
    public UserRoleEntity() {
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserRoleEntity other = (UserRoleEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(role, other.role) && Objects.equals(userId, other.userId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, role, userId);
	}

	@Override
	public String toString() {
		return "UserRoleEntity [id=" + id + ", userId=" + userId + ", role=" + role + "]";
	}	
    
}