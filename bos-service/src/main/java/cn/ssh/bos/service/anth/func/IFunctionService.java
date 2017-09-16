package cn.ssh.bos.service.anth.func;

import cn.ssh.bos.domain.auth.Function;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IFunctionService {
    void save(Function model);

    Page<Function> findPageDate(Pageable pageable);

    List<Function> findAll();
}
