package cn.ssh.bos.dao.qp.workBill;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.ssh.bos.domain.qp.WorkBill;

public interface IWorkBillDao extends JpaRepository<WorkBill, Serializable> ,JpaSpecificationExecutor<WorkBill>{


}
