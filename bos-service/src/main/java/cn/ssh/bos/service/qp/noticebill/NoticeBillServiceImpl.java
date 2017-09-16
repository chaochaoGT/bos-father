package cn.ssh.bos.service.qp.noticebill;

import java.util.Date;
import java.util.Set;

import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.lang3.StringUtils;
import org.apache.cxf.jaxrs.client.WebClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import cn.ssh.bos.dao.bc.IDecidedzoneDao;
import cn.ssh.bos.dao.bc.IRegionDao;
import cn.ssh.bos.dao.qp.noticebill.INoticeBillDao;
import cn.ssh.bos.dao.qp.workBill.IWorkBillDao;
import cn.ssh.bos.domain.bc.Decidedzone;
import cn.ssh.bos.domain.bc.Region;
import cn.ssh.bos.domain.bc.Staff;
import cn.ssh.bos.domain.bc.Subarea;
import cn.ssh.bos.domain.crm.Customers;
import cn.ssh.bos.domain.qp.NoticeBill;
import cn.ssh.bos.domain.qp.WorkBill;
import cn.ssh.bos.service.bc.base.BaseService;
@Service("noticeBillService")
@Transactional
public class NoticeBillServiceImpl implements INoticeBillService,BaseService {

	@Autowired
	private INoticeBillDao noticeBillDao;
	@Autowired
	private IWorkBillDao workBillDao;
	@Autowired
	private IRegionDao regionDao;
	@Autowired
	private IDecidedzoneDao decidedzoneDao;
	@Autowired
	private JmsTemplate jmsTemplate; 
	@Override
	public Customers getCustomer(String telephone) {
		// TODO Auto-generated method stub
		String url=CRM_URL+"getCustomerByTel/"+telephone;
		
		 return  WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(Customers.class);
		
		
	}
	public void save(final NoticeBill model, String province, String city, String district) {
        boolean flag = false;// 作用: 控制crm系统老客户地址是否更新
		noticeBillDao.saveAndFlush(model);// saveAndFlush model瞬时 --->持久态 model OID

		// 2: 取派员 自动分单 成功 分单类型 自动! 如果自动分单失败 staff null 分单类型人工
		// 地址库完全匹配
		//查询客户信息,获取decidedzone的id(最好是匹配地址一个用户多个地址)

		String url = CRM_URL + "findcustomerbyaddress/" + model.getArrivecity()+model.getPickaddress();
        Customers c =WebClient.create(url).accept(MediaType.APPLICATION_JSON).get(Customers.class);
       // System.out.println("完全匹配客户======="+c.getName()+c.getAddress()+c.getDecidedzoneId());
        if (c != null) {
            // 根据地址查询客户信息
            String decidedzoneId = c.getDecidedzoneId();// 断点...

            if (StringUtils.isNotBlank(decidedzoneId)) {
                // 老客户,地址未变,客户已经被关联
                Decidedzone decidedzone = decidedzoneDao.findById(c.getDecidedzoneId());
                final Staff staff = decidedzone.getStaff();
                model.setStaff(staff);
                model.setOrdertype("自动");
                // 自动分单已经成功 生成一张工单
                generateWorkBill(model, staff);
                // 发送短信mq至快递员
                // 地址库完全匹配成功 说明用户地址没有更改 所以crm没有必要去更新客户的地址
                flag = true;
                System.out.println("----地址库完全匹配法-----------------");
                crmCustomer(model, flag);// 不需要更新客户地址和定区id
                smsToMQ(model,staff,jmsTemplate);
                return;
            }
        }
            //未查到客户
            Region region = regionDao.findByCitys(province+"省", city+"市",district);
            System.out.println("管理分区匹配法 省市区信息" + region.getProvince() + region.getCity() + region.getDistrict());
            Set<Subarea> subareas = region.getSubareas();
            if (subareas != null && subareas.size() != 0) {
                for (Subarea sub : subareas) {
                    if (model.getPickaddress().contains(sub.getAddresskey())) {
                        Decidedzone zone = sub.getDecidedzone();
                        if (zone != null) {
                            final Staff staff = zone.getStaff();
                            model.setStaff(staff);
                            model.setOrdertype("自动");
                            generateWorkBill(model, staff);// 生成工单
                            flag = false;
                            crmCustomer(model, flag);// 录入客户信息 更改客户地址 定区id置null
                            // 发送短信mq
                            smsToMQ(model,staff,jmsTemplate);
                            System.out.println("----管理分区匹配法-----------------");
                            return;
                        }

                    }

            }
        }
        crmCustomer(model, flag);// 如果自动分单失败,那么我们根据flag 需要更新crm系统客户信息
        // 老客户如果地址相同不需要修改地址 定区id不变
        // 如果是老客户 地址不同则需要更新crm地址库该用户地址 定区id需要置null重新关联定区和客户关系
        // 如果是新客户 直接插入用户信息即可
        model.setOrdertype("人工");
	}
	


    //发送短信
    private  void smsToMQ(final NoticeBill model, final  Staff staff,JmsTemplate jmsTemplate){
        jmsTemplate.send("bos_staff",  new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                // TODO Auto-generated method stub
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone",  staff.getTelephone());
                mapMessage.setString("staffname", staff.getName());
                mapMessage.setString("username",model.getCustomerName());
                mapMessage.setString("utel", model.getTelephone());
                mapMessage.setString("addr", model.getPickaddress());
                return  mapMessage;
            }
        });
        //发送短信mq至客户
        jmsTemplate.send("bos_customer",  new MessageCreator() {
            @Override
            public Message createMessage(Session session) throws JMSException {
                // TODO Auto-generated method stub
                MapMessage mapMessage = session.createMapMessage();
                mapMessage.setString("telephone", model.getTelephone());
                mapMessage.setString("username", model.getCustomerName());
                mapMessage.setString("staffname", staff.getName());
                mapMessage.setString("stel", staff.getTelephone());
                return  mapMessage;
            }
        });
    }

    //新单
    private void generateWorkBill(final NoticeBill model, final Staff staff) {
        WorkBill bill = new WorkBill();
        bill.setAttachbilltimes(0);
        bill.setBuildtime(new Date(System.currentTimeMillis()));
        bill.setNoticebill(model);
        bill.setType("新");
        bill.setStaff(staff);
        bill.setRemark(model.getRemark());
        bill.setPickstate("新单");
        workBillDao.save(bill);
    }
	private void crmCustomer(final NoticeBill model, boolean flag) {
		System.out.println(model.getId() + "=================" + String.valueOf(model.getCustomerId()));
		// 1 : 客户是否是一个新客户 crm录入 返回 customerId
		if (!("null".equalsIgnoreCase(String.valueOf(model.getCustomerId())))) {
			if (!flag) {
				// flag = false 需要修改crm地址 客户关联的定区id要置null
				// 老客户 更新crm地址
				System.out.println("-----------crm----------老客户地址修改了 !");
				String urlupdate = CRM_URL + "updateCustomerFromNotice/"+model.getCustomerId()
						+"/"+model.getArrivecity()+model.getPickaddress();
               WebClient.create(urlupdate).put(null);
            }

		} else {
			// 新客户 crm系统录入客户信息 返回Cusotmer id String.valueOf(model.getCustomerId()) 新客户id是null字符串
			String urlsave = CRM_URL+ "save/";
			Customers customer = new Customers();
			customer.setName(model.getCustomerName());
			customer.setAddress(model.getArrivecity()+model.getPickaddress());
			customer.setDecidedzoneId(null);
			customer.setStation("传智播客");
			customer.setTelephone(model.getTelephone());
			Response post = WebClient.create(urlsave).accept(MediaType.APPLICATION_JSON).post(customer);
			// 响应体 获取实体模型
             customer = post.readEntity(Customers.class);// 主键从crm系统获取
			model.setCustomerId(customer.getId());// noticebill --->customerId

		}
    }

}
