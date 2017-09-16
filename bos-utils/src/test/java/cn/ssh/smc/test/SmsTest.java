package cn.ssh.smc.test;

import java.util.ArrayList;
import java.util.List;

import com.aliyuncs.exceptions.ClientException;

import cn.alidayu.send.message.SmsSystem;


public class SmsTest {
	public static void main(String[] args) {
		List< String> tels=new ArrayList<>();
		tels.add("18010712035");
		tels.add("17612184868");
		for (String tel : tels) {
			
			try {
				SmsSystem.sendSms(tel, "好好学习,天天看美美");
			} catch (ClientException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
}
