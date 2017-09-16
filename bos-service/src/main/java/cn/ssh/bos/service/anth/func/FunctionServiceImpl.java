package cn.ssh.bos.service.anth.func;

import cn.ssh.bos.dao.auth.IFunctionDao;
import cn.ssh.bos.domain.auth.Function;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */
@Service("functionService")
@Transactional
public class FunctionServiceImpl implements IFunctionService {

    @Autowired
    private IFunctionDao functionDao;
    @Override
    public void save(Function model) {

        functionDao.save(model);
    }

    @Override
    public Page<Function> findPageDate(Pageable pageable) {
        return functionDao.findAll(pageable);
    }

    @Override
    public List<Function> findAll() {
        return functionDao.findAll();
    }
}
