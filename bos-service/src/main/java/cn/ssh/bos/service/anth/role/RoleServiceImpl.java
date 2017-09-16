package cn.ssh.bos.service.anth.role;

import cn.ssh.bos.dao.auth.IFunctionDao;
import cn.ssh.bos.dao.auth.role.IRoleDao;
import cn.ssh.bos.domain.auth.Function;
import cn.ssh.bos.domain.auth.Menu;
import cn.ssh.bos.domain.auth.Role;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2017/7/31.
 */
@Service("roleService")
@Transactional
public class RoleServiceImpl implements IRoleService {
    @Autowired
    private IRoleDao roleDao;

    @Autowired
    private IFunctionDao functionDao;
    @Override
    public void save(Role model, String menuIds, String[] fids) {
        roleDao.saveAndFlush(model);
        System.out.println("获取所哟普的全县========"+fids);
        if (StringUtils.isNotBlank(menuIds)){
            String[] mids = menuIds.split(",");
            Set<Menu> menus = model.getMenus();
            for (String mid:mids) {
                Menu menu=new Menu();
                menu.setId(mid);
                menus.add(menu);
            }
        }
        if (fids != null&&fids.length>0) {
            Set<Function> functions = model.getFunctions();
            for (String fid:fids) {
                Function func=new Function();
                func.setId(fid);
                functions.add(func);
            }
        }
    }

    @Override
    public Page<Role> findPageDate(Pageable pageable) {
        return roleDao.findAll(pageable);
    }

    @Override
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Override
    public List<Role> findAllRoles(Integer uid) {
        List<Role> list = roleDao.findAllRoles(uid);
//        for (Role r :list) {
//            Hibernate.initialize(r.getFunctions());
//        }

        return list;
    }
}
