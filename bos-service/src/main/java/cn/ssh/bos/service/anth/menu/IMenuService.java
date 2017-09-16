package cn.ssh.bos.service.anth.menu;

import cn.ssh.bos.domain.auth.Menu;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IMenuService {
    List<Menu> findAll();

    Page<Menu> findPageDate(Pageable pageable);

    void save(Menu model);

    List<Menu> ajaxList();

    List<Menu> findMenuByUid(Integer id);
}
