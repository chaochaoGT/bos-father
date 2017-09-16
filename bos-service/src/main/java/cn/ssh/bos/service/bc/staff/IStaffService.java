package cn.ssh.bos.service.bc.staff;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import cn.ssh.bos.domain.bc.Staff;
import cn.ssh.bos.domain.bc.Standard;

public interface IStaffService {

	public void save(Staff standard);

	public Page<Staff> findPageDate(Pageable pageable);

	public void deltag(String id);

	public void backtag(String id);

	public Staff findBytelephone(String telephone);

	public Page<Staff> findPageDate(Specification<Staff> spec, Pageable pageable);

	public List<Staff> findAll();

}
