package cn.ssh.bos.service.bc.decidedzoneService;

import java.util.List;

import javax.ws.rs.core.MediaType;

import org.apache.cxf.jaxrs.client.WebClient;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ssh.bos.dao.bc.IDecidedzoneDao;
import cn.ssh.bos.dao.bc.ISubareaDao;
import cn.ssh.bos.domain.bc.Decidedzone;
import cn.ssh.bos.domain.bc.Subarea;
import cn.ssh.bos.domain.crm.Customers;
import cn.ssh.bos.service.bc.base.BaseService;

@Service("decidedzoneService")
@Transactional
@SuppressWarnings("all")
public class DecidedzoneServiceImpl implements IDecidedzoneService,BaseService {

	@Autowired
	private IDecidedzoneDao decidedzoneDao;
	
	@Autowired
	private ISubareaDao subareaDao;
	@Override
	public void save(String[] sid, Decidedzone model) {
		
		 decidedzoneDao.saveAndFlush(model);
		if (sid!=null && sid.length>0) {
			for (String sub : sid) {
				subareaDao.updateSubarea(model,sub);
			}
		}

	}
	@Override
	public Page<Decidedzone> findPageDate(Pageable pageable) {
		Page<Decidedzone> page = decidedzoneDao.findAll(pageable);
		List<Decidedzone> list = page.getContent();
		for (Decidedzone decidedzone : list) {
			Hibernate.initialize(decidedzone.getStaff());
		}
		return page;
	}
	@Override
	public Page<Decidedzone> findPageDate(Specification<Decidedzone> spec, Pageable pageable) {
		Page<Decidedzone> page = decidedzoneDao.findAll(spec,pageable);
		List<Decidedzone> list = page.getContent();
		for (Decidedzone decidedzone : list) {
			Hibernate.initialize(decidedzone.getStaff());
			Hibernate.initialize(decidedzone.getSubareas());
		}
		return page;
	}
//
	@Override
	public void deltag(Decidedzone decidedzone) {
		//查询所有关联的分区
		List<Subarea> subareas=subareaDao.findByDecidedzone(decidedzone);
		//遍历更新分区的did为null,解除分区关联
		if (subareas!=null&&subareas.size()>0) {
			for (Subarea s : subareas) {
				subareaDao.updateSubarea(null, s.getId());
			}
		}
		//解除客户关联
		assigncustomerstodecidedzone(decidedzone.getId(), null);
		decidedzoneDao.delete(decidedzone);
	}
	@Override
	public Decidedzone findById(String id) {
		return decidedzoneDao.findById(id);
	}
	@Override
	public List<Customers> findUserAssociation( String id) {
		// TODO Auto-generated method stub
		String url=CRM_URL+"findUserAssociation/"+id;
		return (List<Customers>) WebClient.create(url).accept(MediaType.APPLICATION_JSON).getCollection(Customers.class);
		 
	}
	@Override
	public List<Customers> findNoAssociation() {
		// TODO Auto-generated method stub
		String url=CRM_URL+"findNoAssociation";
		
		 return (List<Customers>) WebClient.create(url).accept(MediaType.APPLICATION_JSON).getCollection(Customers.class);
		
	}
	@Override
	public void assigncustomerstodecidedzone(String did, String[] cids) {
		// TODO Auto-generated method stub
		String url=CRM_URL+"assigncustomerstodecidedzone/"+did;
		StringBuffer cid=new StringBuffer();
		System.out.println(cids);
		///===A13/none,8,
		if (cids!=null&&cids.length>0) {
			for (String string : cids) {
				cid.append(string).append(",");
			}
			url=url+"/"+cid;
		}else{
			url+="/none";
		}
		WebClient.create(url).put(null);
	}

}
