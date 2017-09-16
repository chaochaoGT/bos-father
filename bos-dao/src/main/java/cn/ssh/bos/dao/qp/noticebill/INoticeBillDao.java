package cn.ssh.bos.dao.qp.noticebill;

import java.io.Serializable;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import cn.ssh.bos.domain.qp.NoticeBill;

public interface INoticeBillDao extends JpaRepository<NoticeBill, Serializable>, JpaSpecificationExecutor<NoticeBill> {

}
