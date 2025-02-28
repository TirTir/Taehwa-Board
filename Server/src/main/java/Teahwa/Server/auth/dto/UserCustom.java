package Teahwa.Server.auth.dto;

import Teahwa.Server.user.entity.User;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Getter
@Setter
@ToString
public class UserCustom extends User implements UserDetails {
    private static final long serialVersionUID = 1L;

    private List<GrantedAuthority> authorities; // 권한 정보 추가

    public UserCustom(User user, Collection<? extends GrantedAuthority> authorities) {
        super(user.getId(), user.getEmail(), user.getUserName(), user.getPassword()); // 부모 클래스 필드 초기화
        this.authorities = (List<GrantedAuthority>) authorities; // GrantedAuthority 리스트 초기화
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public String getUsername() {
        return getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}