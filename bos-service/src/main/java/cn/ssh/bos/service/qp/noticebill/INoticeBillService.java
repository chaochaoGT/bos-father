package cn.ssh.bos.service.qp.noticebill;

import cn.ssh.bos.domain.crm.Customers;
import cn.ssh.bos.domain.qp.NoticeBill;

public interface INoticeBillService {

	Customers getCustomer(String telephone);

	void save(NoticeBill model, String province, String city, String district);

}
