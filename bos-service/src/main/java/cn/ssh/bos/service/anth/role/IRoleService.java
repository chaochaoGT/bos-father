package cn.ssh.bos.service.anth.role;

import cn.ssh.bos.domain.auth.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IRoleService {
    void save(Role model, String menuIds, String[] fids);

    Page<Role> findPageDate(Pageable pageable);

    List<Role> findAll();

    List<Role> findAllRoles(Integer uid);
}
