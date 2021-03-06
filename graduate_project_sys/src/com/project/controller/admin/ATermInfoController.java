package com.project.controller.admin;

import com.project.controller.LoginModel;

import com.project.dao.*;

import com.project.model.*;

import com.project.service.*;
import com.project.service.impl.*;

import com.project.util.*;

import net.sf.json.JSONObject;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;

import org.springframework.ui.ModelMap;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

import java.text.SimpleDateFormat;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@Controller
@RequestMapping("/admin/term_info")
public class ATermInfoController {
    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd");
    @Autowired
    TermInfoService termInfoService;
    @Autowired
    AdminInfoMapper adminInfoMapper;
    @Autowired
    GradeInfoMapper gradeInfoMapper;
    @Autowired
    TermInfoMapper termInfoMapper;

    public void getList(ModelMap modelMap, LoginModel login) {
        GradeInfoExample gradeInfoE = new GradeInfoExample();
        GradeInfoExample.Criteria gradeInfoC = gradeInfoE.createCriteria();
        List<GradeInfo> gradeInfoList = gradeInfoMapper.selectByExample(gradeInfoE);
        List<Map<String, Object>> gradeInfoList2 = new ArrayList<Map<String, Object>>();

        for (GradeInfo m : gradeInfoList) {
            Map<String, Object> map = new HashMap<String, Object>();
            map.put("id", m.getId());
            map.put("name", m.getGname());
            gradeInfoList2.add(map);
        }

        modelMap.addAttribute("gradeInfoList", gradeInfoList2);
    }

    /**
     * ??????????????????jsp??????
    */
    @RequestMapping(value = "")
    public String index(ModelMap modelMap, HttpServletRequest request) {
        LoginModel login = (LoginModel) request.getSession()
                                               .getAttribute(CommonVal.sessionName); //??????????????????????????????
        AdminInfo adminInfo = adminInfoMapper.selectByPrimaryKey(login.getId());
        modelMap.addAttribute("user", adminInfo);
        getList(modelMap, login); //????????????????????????????????????

        return "admin/term_info/list";
    }

    /**
     * ??????????????????????????????????????????,???????????????????????????
    */
    @RequestMapping(value = "queryList")
    @ResponseBody
    public Object toList(TermInfo model, Integer page,
        HttpServletRequest request) {
        LoginModel login = (LoginModel) request.getSession()
                                               .getAttribute(CommonVal.sessionName);

        return termInfoService.getDataList(model, page, CommonVal.pageSize,
            login); //??????????????????
    }

    /**
     ??????????????????
    */
    @RequestMapping("add")
    public String add(ModelMap modelMap, TermInfo model,
        HttpServletRequest request) {
        LoginModel login = (LoginModel) request.getSession()
                                               .getAttribute(CommonVal.sessionName); //???session???????????????????????????	
        getList(modelMap, login); //?????????????????????????????????????????????????????????
        modelMap.addAttribute("data", model);

        return "admin/term_info/add_page";
    }

    /**
     ????????????????????????
    */
    @RequestMapping("add_submit")
    @ResponseBody
    public Object add_submit(TermInfo model, ModelMap modelMap,
        HttpServletRequest request) {
        LoginModel login = (LoginModel) request.getSession()
                                               .getAttribute(CommonVal.sessionName);
        Map<String, Object> rs = new HashMap<String, Object>();
        String msg = termInfoService.add(model, login); //??????????????????

        if (msg.equals("")) {
            rs.put("code", 1);
            rs.put("msg",
                "????????????");

            return rs;
        }

        rs.put("code", 0);
        rs.put("msg", msg);

        return rs;
    }

    /**
     ??????????????????
    */
    @RequestMapping("update")
    public String update(ModelMap modelMap, TermInfo model,
        HttpServletRequest request) {
        LoginModel login = (LoginModel) request.getSession()
                                               .getAttribute(CommonVal.sessionName); //???session???????????????????????????	
        getList(modelMap, login); //?????????????????????????????????????????????????????????

        TermInfo data = termInfoMapper.selectByPrimaryKey(model.getId());
        modelMap.addAttribute("data", data);

        return "admin/term_info/update_page";
    }

    /**
     ????????????????????????
    */
    @RequestMapping("update_submit")
    @ResponseBody
    public Object update_submit(TermInfo model, ModelMap modelMap,
        HttpServletRequest request) {
        LoginModel login = (LoginModel) request.getSession()
                                               .getAttribute(CommonVal.sessionName);
        Map<String, Object> rs = new HashMap<String, Object>();
        String msg = termInfoService.update(model, login); //??????????????????

        if (msg.equals("")) {
            rs.put("code", 1);
            rs.put("msg",
                "????????????");

            return rs;
        }

        rs.put("code", 0);
        rs.put("msg", msg);

        return rs;
    }

    /**
     ??????????????????
    */
    @RequestMapping("del")
    @ResponseBody
    public Object del(Integer id, ModelMap modelMap, HttpServletRequest request) {
        LoginModel login = (LoginModel) request.getSession()
                                               .getAttribute(CommonVal.sessionName);
        Map<String, Object> rs = new HashMap<String, Object>();
        termInfoService.delete(id);
        rs.put("code", 1);
        rs.put("msg",
            "????????????");

        return rs;
    }
}

