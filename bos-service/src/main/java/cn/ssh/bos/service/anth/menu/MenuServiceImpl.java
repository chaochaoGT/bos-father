package cn.ssh.bos.service.anth.menu;

import cn.ssh.bos.dao.auth.menu.IMenuDao;
import cn.ssh.bos.domain.auth.Menu;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */
@Service("menuService")
@Transactional
public class MenuServiceImpl implements  IMenuService {
    @Autowired
    private IMenuDao menuDao;

    @Override
    public List<Menu> findAll() {
        List<Menu> list = menuDao.findAllMenu();
//        for (Menu m : list) {
//            //立刻加载字menu
//            Hibernate.initialize(m.getMenu());
//        }
        return list;

    }

    @Override
    public Page<Menu> findPageDate(Pageable pageable) {
        return menuDao.findAll(pageable);
    }

    @Override
    public void save(Menu model) {
        menuDao.save(model);
    }

    @Override
    public List<Menu> ajaxList() {
        return menuDao.ajaxList();
    }

    @Override
    public List<Menu> findMenuByUid(Integer uid) {
        return menuDao.findMenuByUid(uid);
    }
}
