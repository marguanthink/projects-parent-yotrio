package com.yotrio.pound.controller;


import com.github.pagehelper.PageInfo;
import com.yotrio.common.domain.DataTablePage;
import com.yotrio.pound.domain.Result;
import com.yotrio.pound.domain.SystemProperties;
import com.yotrio.pound.model.Inspection;
import com.yotrio.pound.model.PoundLog;
import com.yotrio.pound.service.IInspectionService;
import com.yotrio.pound.service.IPoundLogService;
import com.yotrio.pound.utils.ResultUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;

/**
 * 报检单控制接口类
 * 模块名称：study-boot com.wangyq.controller
 * 功能说明：<br>
 * 开发人员：wangyiqiang
 * 创建时间： 2018-08-31 下午5:06
 * 系统版本：1.0.0
 **/

@Controller
@RequestMapping("/inspection")
public class InspectionController extends BaseController {

    @Autowired
    private IInspectionService inspectionService;
    @Autowired
    private IPoundLogService poundLogService;
    @Autowired
    private SystemProperties sysProperties;

    /**
     * 添加报检单页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/form.htm"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String inspection(Model model) {

        return "home/inspect_form";
    }

    /**
     * 添加报检单
     *
     * @param inspection
     * @return
     */
    @RequestMapping(value = "/save", method = {RequestMethod.POST})
    @ResponseBody
    public Result save(Inspection inspection) {
        if (inspection == null) {
            return ResultUtil.error("报检单信息不能为空");
        }
        if (StringUtils.isEmpty(inspection.getPlNo())) {
            return ResultUtil.error("过磅单单号不能为空");
        }
        if (StringUtils.isEmpty(inspection.getPlateNo())) {
            return ResultUtil.error("车牌号码不能为空");
        }

        //查询是否已生产过磅记录，未生成的先生成过磅记录
        PoundLog logInDB = poundLogService.findByPoundLogNo(inspection.getPlNo());
        if (logInDB == null) {
            PoundLog poundLog = new PoundLog();
            poundLog.setPoundId(sysProperties.getPoundClientId());
            poundLog.setPoundName(sysProperties.getPoundClientName());
            poundLog.setPoundLogNo(inspection.getPlNo());
            poundLog.setPlateNo(inspection.getPlateNo());
            poundLog.setCreateTime(new Date());
            //如果填了收货单位，先保存
            if (StringUtils.isNotEmpty(inspection.getUnit_name())) {
                poundLog.setUnitName(inspection.getUnit_name());
            }
            int result = poundLogService.save(poundLog);
            if (result < 1) {
                return ResultUtil.error("过磅记录生成失败");
            }
        }

        //校验报检单
        String checkResult = inspectionService.checkFormSave(inspection);
        if (StringUtils.isNotEmpty(checkResult)) {
            return ResultUtil.error(checkResult);
        }

        int saveResult = inspectionService.save(inspection);
//        int saveResult = 1;
        if (saveResult >= 1) {
            return ResultUtil.success("添加成功");
        } else {
            return ResultUtil.error("添加失败");
        }
    }

    /**
     * 获取报检单列表
     *
     * @param dataTablePage 分页条件
     * @param inspection    查询条件
     * @return
     */
    @RequestMapping(value = "/list", method = {RequestMethod.GET})
    @ResponseBody
    public Result list(DataTablePage dataTablePage, Inspection inspection) {
        PageInfo pageInfo = inspectionService.findAllPaging(dataTablePage, inspection);
        return ResultUtil.success(pageInfo.getTotal(), pageInfo.getList());
    }
}