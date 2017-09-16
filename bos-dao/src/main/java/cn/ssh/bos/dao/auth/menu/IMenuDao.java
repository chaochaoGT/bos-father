package cn.ssh.bos.dao.auth.menu;

import cn.ssh.bos.domain.auth.Menu;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IMenuDao  extends JpaRepository<Menu,Serializable> ,JpaSpecificationExecutor<Menu>{
   @Query("from Menu where generatemenu=1 order by zindex desc")
    List<Menu> findAllMenu();

    @Query("from Menu  order by zindex desc")
    List<Menu> ajaxList();

    @Query("from Menu m inner join fetch m.roles r join fetch r.users u where u.id=?1 order by m.zindex desc")
    List<Menu> findMenuByUid(Integer uid);
}
