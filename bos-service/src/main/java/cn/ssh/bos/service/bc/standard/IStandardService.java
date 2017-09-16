package cn.ssh.bos.service.bc.standard;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import cn.ssh.bos.domain.bc.Standard;

public interface IStandardService {

  public void save(Standard standard);


  public Page<Standard> findPageDate(Pageable pageRequest);


  public void deltag(int parseInt);


  public void backtag(int parseInt);


  public List<Standard> findAll();

}
