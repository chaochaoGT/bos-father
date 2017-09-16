<?xml version="1.0" encoding="UTF-8"?>
 <!DOCTYPE validators PUBLIC
  		"-//Apache Struts//XWork Validator 1.0.3//EN"
  		"http://struts.apache.org/dtds/xwork-validator-1.0.3.dtd">
  		
<validators>
<field name="email">
<field-validator type="requiredstring">
	<message key="login_email_error"></message>
</field-validator>
</field>
<field name="password">
<field-validator type="requiredstring">
	<message key="login_pwd_error"></message>
</field-validator>
<field-validator type="stringlength">
	<param name="maxLength">10</param>
	<param name="minLength">3</param>
	<message key="login_pwdlength_error"></message>
</field-validator>
</field>
</validators>