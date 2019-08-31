package org.yadu.service;


import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.yadu.dao.Node;

import net.sf.json.JSONArray;


public interface RegisterService {
    public boolean registerUser(List<Object> params);

    public Map<String, Object> queryUser(Map<String, Object> map);


    public List<Map<String, Object>> queryMeau(String userid);

    public Map<String, Object> queryTjxx(Map<String, Object> map);


    public boolean queryWjsc(HttpServletRequest req, String method);

    public Map queryDetailed(String zj, String method);

    public Map queryQqfw(HttpServletRequest req, String method);

    public Map queryU8Qqfw(HttpServletRequest req, String method);

    public boolean updDetailed(List params, String method);

    public void loginOut();


}
