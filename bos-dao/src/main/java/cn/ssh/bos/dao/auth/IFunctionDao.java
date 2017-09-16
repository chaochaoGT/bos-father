package cn.ssh.bos.dao.auth;

import cn.ssh.bos.domain.auth.Function;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/7/31.
 */
public interface IFunctionDao extends JpaRepository<Function, Serializable>, JpaSpecificationExecutor<Function> {
}
