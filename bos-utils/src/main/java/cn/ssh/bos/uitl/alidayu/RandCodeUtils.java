package cn.ssh.bos.uitl.alidayu;

import java.util.Random;

public class RandCodeUtils {

	public static String getCode() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < 4; i++) {
			sb.append(getRandomString());
		}
		return sb.toString();
	}

	public static int getRandomString() {
		int num = new Random().nextInt(9);
		return num;
	}

	public static void main(String[] args) {
		for (int i = 0; i < 100; i++) {
			System.out.println("随机生成的验证码-->"+getCode());
		}
	}
}
