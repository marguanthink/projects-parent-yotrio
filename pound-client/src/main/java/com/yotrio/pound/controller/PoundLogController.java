package com.yotrio.pound.controller;


import cn.hutool.http.HttpException;
import cn.hutool.http.HttpUtil;
import com.alibaba.fastjson.JSONObject;
import com.yotrio.common.constants.ApiUrlConstant;
import com.yotrio.common.constants.InspectionConstant;
import com.yotrio.common.constants.PoundLogConstant;
import com.yotrio.common.constants.TaskConstant;
import com.yotrio.common.helpers.UserAuthTokenHelper;
import com.yotrio.common.utils.BeansUtil;
import com.yotrio.common.utils.ImageUtil;
import com.yotrio.pound.domain.Result;
import com.yotrio.pound.domain.SystemProperties;
import com.yotrio.pound.model.*;
import com.yotrio.pound.service.*;
import com.yotrio.pound.utils.ResultUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 任务控制接口类
 * 模块名称：study-boot com.wangyq.controller
 * 功能说明：<br>
 * 开发人员：wangyiqiang
 * 创建时间： 2018-08-31 下午5:06
 * 系统版本：1.0.0
 **/

@Controller
@RequestMapping("/poundLog")
public class PoundLogController extends BaseController {

    @Autowired
    private SystemProperties sysProperties;
    @Autowired
    private IPoundLogService poundLogService;
    @Autowired
    private ITaskService taskService;
    @Autowired
    private IInspectionService inspectionService;
    @Autowired
    private IHttpService httpService;
    @Autowired
    private IGoodsService goodsService;
    @Autowired
    private IOrganizationService organizationService;
    @Autowired
    private ICompanyService companyService;


    /**
     * 退货情况下，填写货品信息弹出的页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/outPoundLogForm.htm"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String outPoundLogForm(Model model) {
        List<Goods> goodsList = goodsService.findAll();
        model.addAttribute("goodsList", goodsList);
        return "home/out_pound_log_form";
    }

    /**
     * 进货不填报检单情况下，填写货品信息弹出的页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/inPoundLogForm.htm"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String inPoundLogForm(Model model) {
        List<Goods> goodsList = goodsService.findAll();
        List<Company> companyList = companyService.findAll();
        model.addAttribute("goodsList", goodsList);
        model.addAttribute("companyList", companyList);
        return "home/in_pound_log_form";
    }

    /**
     * 过磅记录详情页面
     *
     * @param model
     * @param poundLogNo
     * @return
     */
    @RequestMapping(value = {"/detail.htm"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String detail(Model model, String poundLogNo) {

        Map<String, Object> map = new HashMap<>(10);
        String token = UserAuthTokenHelper.getUserAuthToken(Integer.valueOf(sysProperties.getPoundClientId()), null);
        map.put("token", token);
        map.put("poundLogNo", poundLogNo);

        //调接口获取过磅信息
        String url = sysProperties.getPoundMasterBaseUrl() + ApiUrlConstant.GET_POUND_LOG_DETAIL;
        try {
            String response = HttpUtil.get(url, map);
            if (StringUtils.isNotEmpty(response)) {
                JSONObject json = JSONObject.parseObject(response);
                if (json.getIntValue("code") == SUCCESS_CODE) {
                    JSONObject data = json.getJSONObject("data");
                    JSONObject logJson = data.getJSONObject("poundLog");
                    PoundLog poundLog = JSONObject.parseObject(logJson.toJSONString(), PoundLog.class);
                    if (poundLog != null) {
                        model.addAttribute("poundLog", poundLog);
                    }
                }
            }
        } catch (Exception e) {
            logger.error("获取过磅详情失败 {}", e.getMessage());
        }
        model.addAttribute("inspectionListUrl", sysProperties.getPoundMasterBaseUrl() + ApiUrlConstant.GET_INSPECTION_LIST);
        model.addAttribute("token", token);
        return "pound/pound_log_detail";
    }

    /**
     * 过磅记录列表页面
     *
     * @param model
     * @return
     */
    @RequestMapping(value = {"/list.htm"}, method = {RequestMethod.GET, RequestMethod.POST})
    public String list(Model model) {
        String token = "";
        Integer poundId = sysProperties.getPoundClientId();
        if (poundId != null) {
            token = UserAuthTokenHelper.getUserAuthToken(poundId, null);
        }
        model.addAttribute("poundId", poundId);
        model.addAttribute("token", token);
        model.addAttribute("goodsList", goodsService.findAll());
        model.addAttribute("poundMasterBaseUrl", sysProperties.getPoundMasterBaseUrl());
        return "pound/pound_log_list";
    }

    /**
     * 获取未处理过磅记录列表以及当前过磅单信息
     *
     * @param poundLogNo 过磅单号
     * @return
     */
    @RequestMapping(value = "/listUnFinishedAndPoundLog", method = {RequestMethod.GET})
    @ResponseBody
    public Result listUnFinishedAndPoundLog(String poundLogNo) {
        Map<String, Object> map = new HashMap<>();
        PoundLog poundLog = null;

        if (poundLogNo != null) {
            poundLog = poundLogService.findByPoundLogNo(poundLogNo);
        }
        List<PoundLog> unFinishedLogs = poundLogService.listUnFinished();

        map.put("unFinishedLogs", unFinishedLogs);
        map.put("poundLog", poundLog);
        return ResultUtil.success(map);
    }

    /**
     * 不填报检单生成 毛重过磅记录
     *
     * @param poundLog
     * @return
     */
    @RequestMapping(value = "/saveGrossWithoutInspection", method = {RequestMethod.POST})
    @ResponseBody
    public Result saveGrossWithoutInspection(PoundLog poundLog) {
        //校验表单
        if (poundLog == null) {
            return ResultUtil.error("过磅信息为空，请先填写报检单相关信息");
        }
        if (poundLog.getGrossWeight() == null) {
            return ResultUtil.error("过磅数据为空，请检查设备是否异常");
        }
        if (StringUtils.isEmpty(poundLog.getPoundLogNo())) {
            return ResultUtil.error("过磅单号为空");
        }
        if (StringUtils.isEmpty(poundLog.getGoodsCode())) {
            return ResultUtil.error("请先选择物料类型");
        }
        PoundLog logInDB = poundLogService.findByPoundLogNo(poundLog.getPoundLogNo());
        if (logInDB != null) {
            return ResultUtil.error("过磅记录已存在");
        }

        //获取物料名称
        String goodsName = goodsService.findGoodsNameByGoodsCode(poundLog.getGoodsCode());
        if (StringUtils.isNotEmpty(goodsName)) {
            poundLog.setGoodsName(goodsName);
        }
        //获取组织名称
        if (StringUtils.isNotEmpty(poundLog.getOrgCode())) {
            String orgName = organizationService.findOrgNameByOrgCode(poundLog.getOrgCode());
            poundLog.setUnitName(orgName);
        }
        //生成过磅单
        poundLog.setPoundId(sysProperties.getPoundClientId());
        poundLog.setPoundName(sysProperties.getPoundClientName());
        poundLog.setFirstTime(new Date());
        poundLog.setTypes(PoundLogConstant.TYPES_IN);
        poundLog.setStatus(PoundLogConstant.STATUS_POUND_FIRST);

        int result = poundLogService.save(poundLog);
        if (result < 1) {
            return ResultUtil.error("称重失败");
        }
        return ResultUtil.success(poundLog);
    }


    /**
     * 更新毛重过磅记录
     *
     * @param poundLog 过磅记录
     * @return
     */
    @RequestMapping(value = "/updateGross", method = {RequestMethod.POST})
    @ResponseBody
    public Result updateGross(PoundLog poundLog) {
        //校验表单
        if (poundLog == null) {
            return ResultUtil.error("过磅信息为空，请先填写报检单相关信息");
        }
        if (poundLog.getGrossWeight() == null) {
            return ResultUtil.error("过磅数据为空，请检查设备是否异常");
        }
        if (StringUtils.isEmpty(poundLog.getPoundLogNo())) {
            return ResultUtil.error("过磅单号为空");
        }
        PoundLog logInDB = poundLogService.findByPoundLogNo(poundLog.getPoundLogNo());
        if (logInDB == null) {
            return ResultUtil.error("请先添加报检单");
        }
        if (logInDB.getStatus() > PoundLogConstant.STATUS_POUND_SECOND) {
            return ResultUtil.error("记录已提交，不能再修改");
        }

        BeansUtil.copyPropertiesIgnoreNull(poundLog, logInDB);

        //判断是退货还是进货
        if (logInDB.getTypes() == PoundLogConstant.TYPES_OUT) {
            //出货
            logInDB.setFirstTime(new Date());
//            logInDB.setNetWeight(logInDB.getGrossWeight() - logInDB.getTareWeight());
            if (logInDB.getStatus() < PoundLogConstant.STATUS_POUND_SECOND) {
                logInDB.setStatus(PoundLogConstant.STATUS_POUND_SECOND);
            }
            if (logInDB.getTareWeight() != null && logInDB.getTareWeight() > 0) {
                if (logInDB.getGrossWeight() < logInDB.getTareWeight()) {
                    return ResultUtil.error("数据异常，毛重不能小于皮重");
                }
                logInDB.setNetWeight(logInDB.getGrossWeight() - logInDB.getTareWeight());
            }
            int result = poundLogService.update(logInDB);
            if (result < 1) {
                return ResultUtil.error("称毛重失败");
            }
            return ResultUtil.success(logInDB);
        } else {
            //进货
            //计算净重 =毛重 - 皮重
            double netWeight = 0.0d;
            double totalReturnWeight = 0.0d;
            double totalSampleWeight = 0.0d;
            double totalInspWeight = 0.0d;
            double tareWeight = logInDB.getTareWeight() != null ? logInDB.getTareWeight() : 0.0d;
            List<Inspection> inspectionList = inspectionService.findListByPlNo(poundLog.getPoundLogNo());
            for (Inspection inspection : inspectionList) {
                if (inspection.getReturnWeight() != null && inspection.getReturnWeight() > 0) {
                    totalReturnWeight += inspection.getReturnWeight();
                }
                if (inspection.getInspWeight() != null && inspection.getInspWeight() > 0) {
                    totalInspWeight += inspection.getInspWeight();
                }
                if (inspection.getTypes() == InspectionConstant.TYPE_SAMPLE) {
                    if (inspection.getInspWeight() != null && inspection.getInspWeight() > 0) {
                        totalSampleWeight += inspection.getInspWeight();
                    }
                }
            }
            //计算磅差和净重,有了皮重之后才可以计算
            if (logInDB.getTareWeight() != null && logInDB.getTareWeight() > 0) {
                netWeight = logInDB.getGrossWeight() - tareWeight;
                logInDB.setNetWeight(netWeight);

                //报检单存在才计算磅差,否则磅差为0
                if (inspectionList != null && inspectionList.size() > 0) {
                    logInDB.setDiffWeight(logInDB.getGrossWeight() - tareWeight - totalInspWeight);
                } else {
                    logInDB.setDiffWeight(0.0d);
                }
            }
            if (logInDB.getTareWeight() != null && logInDB.getGrossWeight() < tareWeight) {
                return ResultUtil.error("数据异常，皮重怎么能比毛重大呢");
            }
            if (netWeight < 0) {
                return ResultUtil.error("数据异常，净重怎么可能小于零");
            }
            logInDB.setReturnWeightTotal(totalReturnWeight);
            logInDB.setFirstTime(new Date());
            if (logInDB.getStatus() < PoundLogConstant.STATUS_POUND_FIRST) {
                logInDB.setStatus(PoundLogConstant.STATUS_POUND_FIRST);
            }

            Integer result = poundLogService.update(logInDB);
            if (result < 1) {
                return ResultUtil.error("更新失败");
            }
            return ResultUtil.success(logInDB);
        }
    }

    /**
     * 更新皮重过磅记录
     *
     * @param poundLog
     * @return
     */
    @RequestMapping(value = "/updateTare", method = {RequestMethod.POST})
    @ResponseBody
    public Result updateTare(PoundLog poundLog) {
        //校验表单
        if (poundLog == null) {
            return ResultUtil.error("过磅信息为空，请先填写报检单相关信息");
        }
        if (poundLog.getTareWeight() == null) {
            return ResultUtil.error("过磅数据为空，请检查设备是否异常");
        }
        if (StringUtils.isEmpty(poundLog.getPoundLogNo())) {
            return ResultUtil.error("过磅单号为空");
        }
        PoundLog logInDB = poundLogService.findByPoundLogNo(poundLog.getPoundLogNo());
        if (logInDB == null) {
            return ResultUtil.error("找不到您要更新的过磅记录");
        }
        if (logInDB.getStatus() > PoundLogConstant.STATUS_POUND_SECOND) {
            return ResultUtil.error("记录已提交，不能再修改");
        }

        BeansUtil.copyPropertiesIgnoreNull(poundLog, logInDB);

        //判断是退货还是进货
        if (logInDB.getTypes() == PoundLogConstant.TYPES_OUT) {
            logInDB.setSecondTime(new Date());
            if (logInDB.getStatus() < PoundLogConstant.STATUS_POUND_FIRST) {
                logInDB.setStatus(PoundLogConstant.STATUS_POUND_FIRST);
            }
            if (logInDB.getGrossWeight() != null && logInDB.getGrossWeight() > 0) {
                if (logInDB.getGrossWeight() < logInDB.getTareWeight()) {
                    return ResultUtil.error("数据异常，毛重不能小于皮重");
                }
                logInDB.setNetWeight(logInDB.getGrossWeight() - logInDB.getTareWeight());
            }
            int result = poundLogService.update(logInDB);
            if (result < 1) {
                return ResultUtil.error("称皮重失败");
            }
            return ResultUtil.success(logInDB);
        } else {
            //计算净重 = 毛重 - 样品 - 皮重
            double netWeight = 0.0d;
            double totalReturnWeight = 0.0d;
            double totalSampleWeight = 0.0d;
            double totalInspWeight = 0.0d;
            double tareWeight = logInDB.getTareWeight() != null ? logInDB.getTareWeight() : 0.0d;

            List<Inspection> inspectionList = inspectionService.findListByPlNo(poundLog.getPoundLogNo());
            //遍历计算各种总重
            for (Inspection inspection : inspectionList) {
                if (inspection.getReturnWeight() != null && inspection.getReturnWeight() > 0) {
                    totalReturnWeight += inspection.getReturnWeight();
                }
                if (inspection.getInspWeight() != null && inspection.getInspWeight() > 0) {
                    totalInspWeight += inspection.getInspWeight();
                }
                if (inspection.getTypes() == InspectionConstant.TYPE_SAMPLE) {
                    if (inspection.getInspWeight() != null && inspection.getInspWeight() > 0) {
                        totalSampleWeight += inspection.getInspWeight();
                    }
                }
            }

            //计算磅差及净重，有了毛重之后才可以计算
            if (logInDB.getGrossWeight() != null && logInDB.getGrossWeight() > 0) {
                //计算净重
                netWeight = logInDB.getGrossWeight() - tareWeight;
                logInDB.setNetWeight(netWeight);

                //报检单存在才计算磅差,否则磅差为0
                if (inspectionList != null && inspectionList.size() > 0) {
                    logInDB.setDiffWeight(logInDB.getGrossWeight() - tareWeight - totalInspWeight);
                } else {
                    logInDB.setDiffWeight(0.0d);
                }
            }

            if (logInDB.getGrossWeight() != null && logInDB.getGrossWeight() < tareWeight) {
                return ResultUtil.error("数据异常，皮重怎么能比毛重大呢");
            }
            if (netWeight < 0) {
                return ResultUtil.error("数据异常，请检查退货数量是否填写有误");
            }
            logInDB.setReturnWeightTotal(totalReturnWeight);
            logInDB.setSecondTime(new Date());
            logInDB.setStatus(PoundLogConstant.STATUS_POUND_SECOND);

            Integer result = poundLogService.update(logInDB);
            if (result < 1) {
                return ResultUtil.error("更新失败");
            }
            return ResultUtil.success(logInDB);
        }
    }

    /**
     * 生成出（退）货 皮重过磅记录
     *
     * @param poundLog
     * @return
     */
    @RequestMapping(value = "/saveReturnTare", method = {RequestMethod.POST})
    @ResponseBody
    public Result saveReturnTare(PoundLog poundLog) {
        //校验表单
        if (poundLog == null) {
            return ResultUtil.error("过磅信息为空，请先填写报检单相关信息");
        }
        if (poundLog.getTareWeight() == null) {
            return ResultUtil.error("过磅数据为空，请检查设备是否异常");
        }
        if (StringUtils.isEmpty(poundLog.getPoundLogNo())) {
            return ResultUtil.error("过磅单号为空");
        }
        PoundLog logInDB = poundLogService.findByPoundLogNo(poundLog.getPoundLogNo());
        if (logInDB != null) {
            return ResultUtil.error("过磅记录已存在");
        }

        //获取物料名称
        String goodsName = goodsService.findGoodsNameByGoodsCode(poundLog.getGoodsCode());
        if (StringUtils.isNotEmpty(goodsName)) {
            poundLog.setGoodsName(goodsName);
        }
        //获取组织名称
        if (StringUtils.isNotEmpty(poundLog.getOrgCode())) {
            String orgName = organizationService.findOrgNameByOrgCode(poundLog.getOrgCode());
            poundLog.setUnitName(orgName);
        }
        //生成退货过磅单
        poundLog.setPoundId(sysProperties.getPoundClientId());
        poundLog.setPoundName(sysProperties.getPoundClientName());
        poundLog.setSecondTime(new Date());
        poundLog.setTypes(PoundLogConstant.TYPES_OUT);
        poundLog.setStatus(PoundLogConstant.STATUS_POUND_FIRST);
        if (StringUtils.isEmpty(poundLog.getRemark())) {
            poundLog.setRemark("出货");
        }
        int result = poundLogService.save(poundLog);
        if (result < 1) {
            return ResultUtil.error("称重失败");
        }
        return ResultUtil.success(poundLog);
    }

    /**
     * 保存过磅单信息
     *
     * @param poundLog
     * @return
     */
    @RequestMapping(value = "/updatePoundLog", method = {RequestMethod.POST})
    @ResponseBody
    public Result poundLog(PoundLog poundLog) {
        //校验表单
        if (poundLog == null) {
            return ResultUtil.error("过磅信息为空，请先填写报检单相关信息");
        }
        if (StringUtils.isEmpty(poundLog.getPoundLogNo())) {
            return ResultUtil.error("过磅单号为空");
        }
        PoundLog logInDB = poundLogService.findByPoundLogNo(poundLog.getPoundLogNo());
        if (logInDB == null) {
            return ResultUtil.error("请先过磅或者填写报检单");
        }
//        if (StringUtils.isEmpty(poundLog.getOrgCode())) {
//            return ResultUtil.error("组织不能为空，请选择组织");
//        }
        //获取组织名称
        String orgName = organizationService.findOrgNameByOrgCode(poundLog.getOrgCode());
        poundLog.setUnitName(orgName);

        if (logInDB.getStatus() > PoundLogConstant.STATUS_POUND_SECOND) {
            return ResultUtil.error("记录已提交，不能再修改");
        }

        BeansUtil.copyPropertiesIgnoreNull(poundLog, logInDB);

        int result = poundLogService.update(logInDB);
        if (result < 1) {
            return ResultUtil.error("称重失败");
        }
        return ResultUtil.success(logInDB);
    }

    /**
     * 上传本地数据到服务器
     *
     * @param poundLogNo 过磅单单号
     * @param orgCode   组织编码
     * @return
     */
    @RequestMapping(value = "/uploadServer", method = {RequestMethod.POST})
    @ResponseBody
    public Result uploadServer(String poundLogNo, String orgCode) {
        if (StringUtils.isEmpty(poundLogNo)) {
            return ResultUtil.error("获取不到过磅单号");
        }
        if (StringUtils.isEmpty(orgCode)) {
            return ResultUtil.error("请先选择组织");
        }
        PoundLog poundLog = poundLogService.findByPoundLogNo(poundLogNo);
        if (poundLog == null) {
            return ResultUtil.error("找不到您要提交的过磅单");
        }
        if (poundLog.getStatus() < PoundLogConstant.STATUS_POUND_SECOND) {
            return ResultUtil.error("请先完成两次称重操作再提交");
        }

        String unitName = organizationService.findOrgNameByOrgCode(orgCode);
        poundLog.setOrgCode(orgCode);
        poundLog.setUnitName(unitName);
        //保存组织信息
        poundLogService.update(poundLog);

        String msg = "网络连接失败,联网后系统会自动为您提交";
        //获取关联的报检单
        List<Inspection> inspections = inspectionService.findListByPlNo(poundLogNo);
        //进货要计算报每张检单称重结果，按报检单上重量 的比例分配
        if (poundLog.getTypes() == PoundLogConstant.TYPES_IN && inspections.size() > 0) {
            inspectionService.countInspNetWeight(inspections, poundLog);
        }

        //将本地图片url转base64字符串上传，上传成功后再保存线上服务器
        if (StringUtils.isNotEmpty(poundLog.getGrossImgUrl())) {
            String grossImgFilePath = sysProperties.getFileLocation() + poundLog.getGrossImgUrl().substring((sysProperties.getLocalhostUrl() + sysProperties
                    .getServerPort()).length());
            String grossImgUrlBase64 = ImageUtil.getImageBase64Str(grossImgFilePath);
            if (StringUtils.isNotEmpty(grossImgUrlBase64)) {
                poundLog.setGrossImgUrlBase64(grossImgUrlBase64);
            }
        }
        if (StringUtils.isNotEmpty(poundLog.getTareImgUrl())) {
            String tareImgFilePath = sysProperties.getFileLocation() + poundLog.getTareImgUrl().substring((sysProperties.getLocalhostUrl() + sysProperties
                    .getServerPort()).length());
            String tareImgUrlBase64 = ImageUtil.getImageBase64Str(tareImgFilePath);
            if (StringUtils.isNotEmpty(tareImgUrlBase64)) {
                poundLog.setTareImgUrlBase64(tareImgUrlBase64);
            }
        }

        JSONObject data = new JSONObject();
        data.put("poundLog", poundLog);
        data.put("inspections", inspections);
        Map<String, Object> map = new HashMap<>(10);
        map.put("data", data.toJSONString());
        String token = UserAuthTokenHelper.getUserAuthToken(Integer.valueOf(sysProperties.getPoundClientId()), null);
        map.put("token", token);
        map.put("poundLog", JSONObject.toJSON(poundLog));
        //上传线上服务器
        String url = sysProperties.getPoundMasterBaseUrl() + ApiUrlConstant.SAVE_POUND_LOG;
        try {
            String response = HttpUtil.post(url, map);
            if (StringUtils.isNotEmpty(response)) {
                JSONObject json = JSONObject.parseObject(response);
                msg = json.getString("msg");
                if (json.getIntValue("code") == SUCCESS_CODE) {
                    //上传成功更新本地过磅单状态为待打印
                    poundLog.setStatus(PoundLogConstant.STATUS_WAIT_PRINT);
                    poundLogService.update(poundLog);

                    return ResultUtil.success(poundLog);
                } else {
                    return ResultUtil.error(msg);
                }
            }
        } catch (HttpException e) {
            //网络异常，断网状态，创建任务，网络通后去提交
            Task taskInDB = taskService.findByOtherId(poundLogNo);
            if (taskInDB != null) {
                return ResultUtil.error("已生成任务，请勿重复提交");
            }
            Task task = new Task();
            task.setStatus(TaskConstant.STATUS_INIT);
            task.setOtherId(String.valueOf(poundLog.getPoundLogNo()));
            task.setWeight(TaskConstant.WEIGHT_INIT);
            task.setTypes(TaskConstant.TYPE_UPLOAD_MSG);
            task.setTaskName("上传过磅记录失败|plNo=" + poundLog.getPoundLogNo());
            task.setDescription(msg);
            taskService.save(task);
            //更改过磅单状态网络断开，定时任务执行上传
            poundLog.setStatus(PoundLogConstant.STATUS_NET_DISCONNECT);
            poundLogService.update(poundLog);
            logger.error("上传过磅单失败={}", e.getMessage());
            return ResultUtil.error(msg, poundLog);
        } catch (Exception e) {
            logger.error("上传过磅单失败={}", e);
        }

        return ResultUtil.error("上传过磅单失败!");
    }

    /**
     * 点击了打印按钮,更改状态
     *
     * @return
     */
    @RequestMapping(value = "/doPrint", method = {RequestMethod.GET})
    @ResponseBody
    public Result doPrint(String poundLogNo) {
        if (StringUtils.isEmpty(poundLogNo)) {
            return ResultUtil.error("找不到您要打印的过磅单单号");
        }
        PoundLog poundLog = poundLogService.findByPoundLogNo(poundLogNo);
        if (poundLog == null) {
            return ResultUtil.error("找不到您要打印的过磅单");
        }
        poundLog.setStatus(PoundLogConstant.STATUS_POUND_FINISH);
        int result = poundLogService.update(poundLog);
        if (result < 1) {
            return ResultUtil.error("更新打印状态失败");
        }
        //返回过磅单和报检单给打印模板用
        List<Inspection> inspections = inspectionService.findListByPlNo(poundLogNo);
        poundLog.setInspections(inspections);
        return ResultUtil.success(poundLog);
    }

    /**
     * 删除过磅单及相应的报检单
     *
     * @param poundLogNo
     * @return
     */
    @RequestMapping(value = "/destroy", method = {RequestMethod.GET})
    @ResponseBody
    public Result destroy(String poundLogNo) {
        if (StringUtils.isEmpty(poundLogNo)) {
            return ResultUtil.error("找不到您要删除的过磅单单号");
        }
        poundLogService.destroyPoundLogAndInspections(poundLogNo);
        return ResultUtil.success("删除成功");
    }

    /**
     * 获取过磅信息
     *
     * @param poundLogNo
     * @return
     */
    @RequestMapping(value = "/getLogInfo", method = {RequestMethod.GET})
    @ResponseBody
    public Result getLogInfo(String poundLogNo) {
        if (StringUtils.isEmpty(poundLogNo)) {
            return ResultUtil.error("找不到过磅单单号");
        }
        PoundLog poundLogInDB = poundLogService.findByPoundLogNo(poundLogNo);
        if (poundLogInDB == null) {
            return ResultUtil.error("找不到过磅单");
        }
        return ResultUtil.success(poundLogInDB);
    }
}
