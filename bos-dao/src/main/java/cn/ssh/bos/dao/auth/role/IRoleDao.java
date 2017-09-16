package cn.ssh.bos.dao.auth.role;

import cn.ssh.bos.domain.auth.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IRoleDao extends JpaRepository<Role,Serializable>,JpaSpecificationExecutor<Role> {
    @Query("from Role r join fetch r.functions f join fetch r.users u where u.id=?")
    List<Role> findAllRoles(Integer uid);
}
