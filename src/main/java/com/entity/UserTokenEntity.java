package com.entity;

import java.util.Date;
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
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "USER_TOKEN_TBL")
public class UserTokenEntity extends BaseEntity {
	

	private static final long serialVersionUID = 5753884725531743259L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
    private Long id;
	
	@ManyToOne(targetEntity=UserEntity.class,fetch=FetchType.EAGER)
	@JoinColumn(name="USER_ID",referencedColumnName="ID", nullable = false)
	private UserEntity user;

    @Column(name = "TOKEN")
    private String token;
    
    @Column(name = "IP")
    private String ip;
    
    @Column(name = "ROLES")
    private String roles;
 
	@Column(name ="EXPIRY_DATE", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	Date expiryDate;
	
	@SuppressWarnings("unchecked")
	public Class<?> getEntityClass() {
		return UserTokenEntity.class;
	}
	
    public UserTokenEntity() {}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		result = prime * result + Objects.hash(id, ip, roles, token, user);
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (!super.equals(obj))
			return false;
		if (getClass() != obj.getClass())
			return false;
		UserTokenEntity other = (UserTokenEntity) obj;
		return Objects.equals(id, other.id) && Objects.equals(ip, other.ip) && Objects.equals(roles, other.roles)
				&& Objects.equals(token, other.token) && Objects.equals(user, other.user);
	}

	@Override
	public String toString() {
		return "UserTokenEntity [id=" + id + ", user=" + user + ", token=" + token + ", ip=" + ip + ", roles=" + roles
				+ "]";
	}
    
}