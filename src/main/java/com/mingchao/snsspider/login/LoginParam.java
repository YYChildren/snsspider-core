package com.mingchao.snsspider.login;

import java.util.Map;

public interface LoginParam extends LoginControl{
  
  void setTint(String key, String tint);
  
  void setTint(String key, String tint, boolean hidden);
  
  void setTint(String key, String tint, Object extInfo);
  
  Map<String,String> getLoginInfo();
  
}
