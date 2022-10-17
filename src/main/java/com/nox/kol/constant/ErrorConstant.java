package com.nox.kol.constant;

/**
 * 错误码的常量类
 */
public class ErrorConstant {

    public static final int ERROR_NUM_1001 = 1001;
    public static final String ERROR_MESSAGE_1001_STR = "google accessToken is required";

    public static final int ERROR_NUM_1002 = 1002;
    public static final String ERROR_MESSAGE_1002_STR = "google refreshToken is required";

    public static final int ERROR_NUM_1003 = 1003;
    public static final String ERROR_MESSAGE_1003_STR = "login type is not supported";

    public static final int ERROR_NUM_1004 = 1004;
    public static final String ERROR_MESSAGE_1004_STR = "user_id is required";

    public static final int ERROR_NUM_1005 = 1005;
    public static final String ERROR_MESSAGE_1005_STR = "Please add starInfo";

    public static final int ERROR_NUM_1006 = 1006;
    public static final String ERROR_MESSAGE_1006_STR = "Fetch new accessToken fail";

    public static final int ERROR_NUM_10001 = 10001;
    public static final String ERROR_MESSAGE_10001_STR = "网红的擅长类型不能为空";

    public static final int ERROR_NUM_10008 = 10008;
    public static final String ERROR_MESSAGE_10008_STR = "任务的时间限制不能为空";

    public static final int ERROR_NUM_10009 = 10009;
    public static final String ERROR_MESSAGE_10009_STR = "已创建过同种产品项目";

    public static final int ERROR_NUM_10010 = 10010;
    public static final String ERROR_MESSAGE_10010_STR = "错误的产品链接格式";

    public static final int ERROR_NUM_10017 = 10017;
    public static final String ERROR_MESSAGE_10017_STR = "任务未审核通过原因";

    public static final int ERROR_NUM_100101 = 100101;
    public static final String ERROR_MESSAGE_100101_STR = "广告主不存在";

    public static final int ERROR_NUM_10020 = 10020;
    public static final String ERROR_MESSAGE_10020_STR = "该网红已经存在paypal账号信息，只能做修改操作";

    public static final int ERROR_NUM_10022 = 10022;
    public static final String ERROR_MESSAGE_10022_STR = "参数为 empty";

    public static final int ERROR_NUM_10024 = 10024;
    public static final String ERROR_MESSAGE_10024_STR = "需求类型必须选一个";

    public static final int ERROR_NUM_10025 = 10025;
    public static final String ERROR_MESSAGE_10025_STR = "该广告主已经是要体验的会员级别或以上，无法参与体验会员";

    public static final int ERROR_NUM_10026 = 10026;
    public static final String ERROR_MESSAGE_10026_STR = "无权限访问";

    public static final int ERROR_NUM_10027 = 10027;
    public static final String ERROR_MESSAGE_10027_STR = "主账号会员过期";

    public static final int ERROR_NUM_10029 = 10029;
    public static final String ERROR_MESSAGE_10029_STR = "请求youtube失败";

    public static final int ERROR_NUM_10030 = 10030;
    public static final String ERROR_MESSAGE_10030_STR = "该网红已经存在电汇信息，只能做修改操作";

    public static final int ERROR_NUM_10031 = 10031;
    public static final String ERROR_MESSAGE_10031_STR = "创建任务时自助模式时必须填写受众区域";

    public static final int ERROR_NUM_10033 = 10033;
    public static final String ERROR_MESSAGE_10033_STR = "创建任务时自助模式时必须填写产品类型";

    //网红广告的必填信息
    public static final int ERROR_NUM_10300 = 10300;
    public static final String ERROR_MESSAGE_10300_STR = "广告的id不能为空";

    public static final int ERROR_NUM_10304 = 10304;
    public static final String ERROR_MESSAGE_10304_STR = "此状态不能上传视屏链接";

    public static final int ERROR_NUM_10305 = 10305;
    public static final String ERROR_MESSAGE_10305_STR = "该网红已经申请过该offer";

    public static final int ERROR_NUM_10306 = 10306;
    public static final String ERROR_MESSAGE_10306_STR = "网红不存在";

    public static final int ERROR_NUM_10307 = 10307;
    public static final String ERROR_MESSAGE_10307_STR = "数据不能重复";

    public static final int ERROR_NUM_10308 = 10308;
    public static final String ERROR_MESSAGE_10308_STR = "粉丝数不足1000，不能参与平台发布的任务";

    //通用属性的必填信息
    public static final int ERROR_NUM_10400 = 10400;
    public static final String ERROR_MESSAGE_10400_STR = "ID不能为空";

    public static final int ERROR_NUM_10404 = 10404;
    public static final String ERROR_MESSAGE_10404_STR = "属性异常";

    public static final int ERROR_NUM_10403 = 10403;
    public static final String ERROR_MESSAGE_10403_STR = "系统参数未设定";

    //业务异常的状态码
    public static final int ERROR_NUM_20000 = 20000;
    public static final String ERROR_JSON_ERROR = "JSON转换异常";

    public static final int ERROR_NUM_20001 = 20001;
    public static final String ERROR_MESSAGE_20001_ERROR = "该网红观看受众信息为空";

    public static final int ERROR_NUM_20003 = 20003;
    public static final String ERROR_MESSAGE_20003_ERROR = "该网红观看受众信息数据异常";

    public static final int ERROR_NUM_20005 = 20005;
    public static final String ERROR_20005_ERROR = "相关实体不存在";

    public static final int ERROR_NUM_20006 = 20006;
    public static final String ERROR_20006_ERROR = "确认网红发布视频失败";

    public static final int ERROR_NUM_20009 = 20009;
    public static final String ERROR_20009_ERROR = "状态异常";

    public static final int ERROR_NUM_20012 = 20012;
    public static final String ERROR_20012_ERROR = "新增广告主信息异常";

    public static final int ERROR_NUM_20015 = 20015;
    public static final String ERROR_20015_ERROR = "该youtube频道粉丝量被隐藏";

    public static final int ERROR_NUM_20017 = 20017;
    public static final String ERROR_20017_ERROR = "任务预算必须大于0";

    public static final int ERROR_NUM_20018 = 20018;
    public static final String ERROR_20018_ERROR = "添加任务合作类型参数错误";

    public static final int ERROR_NUM_20037 = 20037;
    public static final String ERROR_20037_ERROR = "vip 操作的uid不能为空";

    public static final int ERROR_NUM_20020 = 20020;
    public static final String ERROR_20020_ERROR = "任务的投放区域不能为空（受众区域）";

    public static final int ERROR_NUM_20034 = 20034;
    public static final String ERROR_20034_ERROR = "vip 等级异常";

    public static final int ERROR_NUM_20039 = 20039;
    public static final String ERROR_20039_ERROR = "修改vip信息异常";

    public static final int ERROR_NUM_20021 = 20021;
    public static final String ERROR_20021_ERROR = "插入广告主对网红评价异常";

    public static final int ERROR_NUM_20026 = 20026;
    public static final String ERROR_20026_ERROR = "获取网红评论列表异常";

    public static final int ERROR_NUM_20022 = 20022;
    public static final String ERROR_20022_ERROR = "uid不能为空";

    public static final int ERROR_NUM_20024 = 20024;
    public static final String ERROR_20024_ERROR = "网红ID不能为空";

    public static final int ERROR_NUM_20025 = 20022;
    public static final String ERROR_20025_ERROR = "插入网红对广告主评价异常";

    public static final int ERROR_NUM_20027 = 20027;
    public static final String ERROR_20027_ERROR = "获取网红评论列表异常";

    public static final int ERROR_NUM_20028 = 20028;
    public static final String ERROR_20028_ERROR = "上传的链接不是youtube视频链接";

    public static final int ERROR_NUM_20030 = 20030;
    public static final String ERROR_20030_ERROR = "youtube 视频非公开或者已删除";

    public static final int ERROR_NUM_20031 = 20031;
    public static final String ERROR_MESSAGE_20031_ERROR = "数据尚未采集";

    public static final int ERROR_NUM_30001 = 30001;
    public static final String ERROR_30001_ERROR = "该网红不存在paypal支付方式请添加";

    public static final int ERROR_NUM_30002 = 30002;
    public static final String ERROR_30002_ERROR = "网络异常";

    public static final int ERROR_NUM_30003 = 30003;
    public static final String ERROR_30003_ERROR = "该网红不存在电汇支付方式请添加";

    public static final int ERROR_NUM_30004 = 30004;
    public static final String ERROR_30004_ERROR = "链接有误，请检查格式后重新输入";

    public static final int ERROR_NUM_30005 = 30005;
    public static final String ERROR_30005_ERROR = "参数错误";

    public static final int ERROR_NUM_30012 = 30012;
    public static final String ERROR_30012_ERROR = "YouTube频道不存在，请检查链接";

    public static final int ERROR_NUM_30010 = 30010;
    public static final String ERROR_30010_ERROR = "YouTube视屏不存在，请检查链接";

    public static final int ERROR_NUM_30011 = 30011;
    public static final String ERROR_30011_ERROR = "此网红数据采集中，请稍后查看";

    public static final int ERROR_NUM_30014 = 30014;
    public static final String ERROR_30014_ERROR = "获取视频信息异常";

    public static final int ERROR_NUM_30015 = 30015;
    public static final String ERROR_30015_ERROR = "根据videoId未查到视频视频信息";

    public static final int ERROR_NUM_30016 = 30016;
    public static final String ERROR_30016_ERROR = "更新视频tags失败";

    public static final int ERROR_NUM_30017 = 30017;
    public static final String ERROR_30017_ERROR = "已经添加该视频";

    public static final int ERROR_NUM_30018 = 30018;
    public static final String ERROR_30018_ERROR = "code已失效";

    public static final int ERROR_NUM_20058 = 20058;
    public static final String ERROR_20058_ERROR = "文件异常";

    public static final int ERROR_NUM_30019 = 30019;
    public static final String ERROR_30019_ERROR = "查询敦煌网订单异常";

    public static final int ERROR_NUM_30020 = 30020;
    public static final String ERROR_30020_ERROR = "此订单已经激活过会员";

    public static final int ERROR_NUM_40000 = 40000;
    public static final String ERROR_40000_ERROR = "uid不能为空,请先登陆";

    public static final int ERROR_NUM_40001 = 40001;
    public static final String ERROR_40001_ERROR = "此Ip目前无法支持此网红";

    public static final int ERROR_NUM_40002 = 40002;
    public static final String ERROR_40002_ERROR = "评论内容为空";

    public static final int ERROR_NUM_40003 = 40003;
    public static final String ERROR_40003_ERROR = "验证身份异常";

    public static final int ERROR_NUM_40006 = 40006;
    public static final String ERROR_40006_ERROR = "必须是此offer的发布人才能接受";

    public static final int ERROR_NUM_40007 = 40007;
    public static final String ERROR_40007_ERROR = "网红申请已经被接受了，不需要重复接受";

    public static final int ERROR_NUM_40008 = 40008;
    public static final String ERROR_40008_ERROR = "必须是此offer的发布人才能接受";

    public static final int ERROR_NUM_40009 = 40009;
    public static final String ERROR_40009_ERROR = "已经被接受或者已经被拒绝了，请刷新页面";

    public static final int ERROR_NUM_40011 = 40011;
    public static final String ERROR_40011_ERROR = "已经处于创作中状态了，请刷新页面";

    public static final int ERROR_NUM_40012 = 40012;
    public static final String ERROR_40012_ERROR = "必须是此offer的申请人才能开始创作";

    public static final int ERROR_NUM_40010 = 40010;
    public static final String ERROR_40010_ERROR = "发送邮件失败";

    public static final int ERROR_NUM_40014 = 40014;
    public static final String ERROR_40014_ERROR = "拒绝原因至少选择一个";

    public static final int ERROR_NUM_40015 = 40015;
    public static final String ERROR_40015_ERROR = "要更新的合同不存在";

    public static final int ERROR_NUM_40016 = 40016;
    public static final String ERROR_40016_ERROR = "达到限额，请提升会员等级";

    public static final int ERROR_NUM_40017 = 40017;
    public static final String ERROR_40017_ERROR = "达到限额，已经是最高等级的会员服务";

    public static final int ERROR_NUM_40018 = 40018;
    public static final String ERROR_40018_ERROR = "此账号未注册，请提醒他先注册NoxInfluencer广告主账号";

    public static final int ERROR_NUM_40019 = 40019;
    public static final String ERROR_40019_ERROR = "此功能仅定制版才支持";

    public static final int ERROR_NUM_40020 = 40020;
    public static final String ERROR_40020_ERROR = "账号已经关联，请更换其他账号";

    public static final int ERROR_NUM_40021 = 40021;
    public static final String ERROR_40021_ERROR = "此账号当前是会员身份，无法添加，请更换账号";

    public static final int ERROR_NUM_40022 = 40022;
    public static final String ERROR_40022_ERROR = "请检查并使用邮件内的链接";

    public static final int ERROR_NUM_40023 = 40023;
    public static final String ERROR_40023_ERROR = "此账号是网红账号，请更换账号";

    public static final int ERROR_NUM_40024 = 40024;
    public static final String ERROR_40024_ERROR = "登录账号与被邀请账号不匹配";

    public static final int ERROR_NUM_40025 = 40025;
    public static final String ERROR_40025_ERROR = "链接过期（超过30天）";

    public static final int ERROR_NUM_40026 = 40026;
    public static final String ERROR_40026_ERROR = "子账号数量已达到上限，请联系主账号";

    public static final int ERROR_NUM_40028 = 40028;
    public static final String ERROR_40028_ERROR = "未查到该账号的关联父账号内容";

    public static final int ERROR_NUM_70004 = 70004;
    public static final String ERROR_70004_ERROR = "网红更新数据异常";

    public static final int ERROR_NUM_70010 = 70010;
    public static final String ERROR_70010_ERROR = "不存在该网红的合作详情";

    public static final int ERROR_NUM_70011 = 70011;
    public static final String ERROR_70011_ERROR = "服务异常";

    public static final int ERROR_NUM_70012 = 70012;
    public static final String ERROR_70012_ERROR = "根据channelId查询网红id为空";

    public static final int ERROR_NUM_70014 = 70014;
    public static final String ERROR_70014_ERROR = "开通会员异常";

    public static final int ERROR_NUM_70015 = 70015;
    public static final String ERROR_70015_ERROR = "无可导出数据";

    public static final int ERROR_NUM_50005 = 50005;
    public static final String ERROR_50005_ERROR = "创建支付账号，网红id不能为空";

    public static final int ERROR_NUM_50006 = 50006;
    public static final String ERROR_50006_ERROR = "创建支付账号，网红不存在";

    public static final int ERROR_NUM_50007 = 50007;
    public static final String ERROR_50007_ERROR = "提现，网红id必传";

    public static final int ERROR_NUM_50008 = 50008;
    public static final String ERROR_50008_ERROR = "提现，网红不存在";

    public static final int ERROR_NUM_50009 = 50009;
    public static final String ERROR_50009_ERROR = "提现，account id 必传";

    public static final int ERROR_NUM_50010 = 50010;
    public static final String ERROR_50010_ERROR = "提现，account id 不存在";

    public static final int ERROR_NUM_50011 = 50011;
    public static final String ERROR_50011_ERROR = "可提现金额不足";

    public static final int ERROR_NUM_50012 = 50012;
    public static final String ERROR_50012_ERROR = "提现金额不能小于等于0";

    public static final int ERROR_NUM_50015 = 50015;
    public static final String ERROR_50015_ERROR = "paypal 账号不存在";

    public static final int ERROR_NUM_51004 = 51004;
    public static final String ERROR_51004_ERROR = "返回的短连接为空";

    public static final int ERROR_NUM_51005 = 51005;
    public static final String ERROR_51005_ERROR = "原始链接已经存在对应的短连接了，请修改原始链接后再生成对应的短连接";

    public static final int ERROR_NUM_51006 = 51006;
    public static final String ERROR_51006_ERROR = "原始链接 必填一个";

    public static final int ERROR_NUM_51007 = 51007;
    public static final String ERROR_51007_ERROR = "短连接格式错误";

    public static final int ERROR_NUM_51008 = 51008;
    public static final String ERROR_51008_ERROR = "短连接非noxinfluencer提供";

    public static final int ERROR_NUM_52001 = 52001;
    public static final String ERROR_52001_ERROR = "生成微信二维码支付url异常";

    public static final int ERROR_NUM_52002 = 52002;
    public static final String ERROR_52002_ERROR = "查询微信订单异常";

    public static final int ERROR_NUM_52003 = 52003;
    public static final String ERROR_52003_ERROR = "生成支付宝二维码支付url异常";

    public static final int ERROR_NUM_52005 = 52005;
    public static final String ERROR_52005_ERROR = "余额不足以提现，金额低于最低限额";

    public static final int ERROR_NUM_52006 = 52006;
    public static final String ERROR_52006_ERROR = "提现失败";

    public static final int ERROR_NUM_52007 = 52007;
    public static final String ERROR_52007_ERROR = "新增交易记录失败";

    public static final int ERROR_NUM_52008 = 52008;
    public static final String ERROR_52008_ERROR = "收款人信息异常";

    public static final int ERROR_NUM_52009 = 52009;
    public static final String ERROR_52009_ERROR = "关闭微信订单异常";

    public static final int ERROR_NUM_52010 = 52010;
    public static final String ERROR_52010_ERROR = "generate braintree client token error";

    public static final int ERROR_NUM_52011 = 52011;
    public static final String ERROR_52011_ERROR = "braintree payments failure";

    public static final int ERROR_NUM_52012 = 52012;
    public static final String ERROR_52012_ERROR = "PayPal 生成订单失败";

    public static final int ERROR_NUM_52013 = 52013;
    public static final String ERROR_52013_ERROR = "PayPal 查询订单状态失败";

    public static final int ERROR_NUM_52014 = 52014;
    public static final String ERROR_52014_ERROR = "订单号错误、或者订单状态异常";

    public static final int ERROR_NUM_52015 = 52015;
    public static final String ERROR_52015_ERROR = "剩余配额不足，请充值";

    public static final int ERROR_NUM_52017 = 52017;
    public static final String ERROR_52017_ERROR = "Stripe 生成订单失败";

    public static final int ERROR_NUM_52018 = 52018;
    public static final String ERROR_52018_ERROR = "取消订阅付款失败";

    public static final int ERROR_NUM_52019 = 52019;
    public static final String ERROR_52019_ERROR = "商品不存在";

    public static final int ERROR_NUM_71001 = 71001;
    public static final String ERROR_71001_ERROR = "此用户不存在noxKey";

    public static final int ERROR_NUM_90000 = 90000;
    public static final String ERROR_90000_ERROR = "不存在邮箱授权信息";

    public static final int ERROR_NUM_91000 = 91000;
    public static final String ERROR_91000_ERROR = "获取access_token异常";

    public static final int ERROR_NUM_91001 = 91001;
    public static final String ERROR_91001_ERROR = "发送gmail错误";

    public static final int ERROR_NUM_91002 = 91002;
    public static final String ERROR_91002_ERROR = "gmail %s 此邮箱不属于此用户";

    public static final int ERROR_NUM_91003 = 91003;
    public static final String ERROR_91003_ERROR = "gmail %s 获取邮件列表异常";

    public static final int ERROR_NUM_91004 = 91004;
    public static final String ERROR_91004_ERROR = "gmail %s threadId %s 获取邮件详情异常";

    public static final int ERROR_NUM_91005 = 91005;
    public static final String ERROR_91005_ERROR = "收件人列表为空";

    public static final int ERROR_NUM_91006 = 91006;
    public static final String ERROR_91006_ERROR = "邮件信息异常";

    public static final int ERROR_NUM_91007 = 91007;
    public static final String ERROR_91007_ERROR = "无导出权限";

    public static final int ERROR_NUM_91008 = 91008;
    public static final String ERROR_91008_ERROR = "邮箱地址或密码错误";

    public static final int ERROR_NUM_91009 = 91009;
    public static final String ERROR_91009_ERROR = "服务器地址异常";

    public static final int ERROR_NUM_91010 = 91010;
    public static final String ERROR_91010_ERROR = "官方模板,无操作权限";


    public static final int ERROR_NUM_92001 = 92001;
    public static final String ERROR_92001_ERROR = "不存在此优惠码";

    public static final int ERROR_NUM_92002 = 92002;
    public static final String ERROR_92002_ERROR = "优惠码与当前选择会员版本、时长、支付币种不符";

    public static final int ERROR_NUM_92003 = 92003;
    public static final String ERROR_92003_ERROR = "定时发送时间不能小于当前时间";

    public static final int ERROR_NUM_92004 = 92004;
    public static final String ERROR_92004_ERROR = "此用户未领用过该优惠券";

    public static final int ERROR_NUM_92005 = 92005;
    public static final String ERROR_92005_ERROR = "此优惠券已使用";

    public static final int ERROR_NUM_80001 = 80001;
    public static final String ERROR_80001_ERROR = "uid无效，不是有效的登录用户";

    public static final int ERROR_NUM_90001 = 90001;
    public static final String ERROR_90001_ERROR = "URL参数异常";

    public static final int ERROR_NUM_90002 = 90002;
    public static final String ERROR_90002_ERROR = "没有找到相关配置";

    public static final int ERROR_NUM_90003 = 90003;
    public static final String ERROR_90003_ERROR = "相关配置已经存在";

    public static final int ERROR_NUM_SERVER_EXCEPTION_99999 = 99999;
    public static final String ERROR_99999_MESSAGE = "参数错误";
    // 榜单自定义导出字段错误吗
    public static final Integer ERROR_NUM_50000 = 50000;
    public static final String ERROR_50000_MESSAGE = "通过传入的榜单筛选条件，未查询到符合条件的频道id";

    //推广项目错误码
    public static final int ERROR_NUM_40013 = 40013;
    public static final String ERROR_40013_ERROR = "该id的项目不存在";

    public static final int ERROR_NUM_40027 = 40027;
    public static final String ERROR_40027_ERROR = "非项目创建者不可修改协作成员";


    // 品牌监控
    public static final int ERROR_NUM_40100 = 40100;
    public static final String ERROR_40100_ERROR = "已经添加过相同的品牌监控";

    public static final int ERROR_NUM_40101 = 40101;
    public static final String ERROR_40101_ERROR = "无删除品牌监控示例权限";

    public static final int ERROR_NUM_40102 = 40102;
    public static final String ERROR_40102_ERROR = "非品牌监控示例无法删除";

    public static final int ERROR_NUM_40103 = 40103;
    public static final String ERROR_40103_ERROR = "品牌监控数量已经达到上限";

    public static final int ERROR_NUM_40104 = 40104;
    public static final String ERROR_40104_ERROR = "品牌监控无数据";

    public static final int ERROR_NUM_40105 = 40105;
    public static final String ERROR_40105_ERROR = "品牌监控数据正在爬取";

    // 网红资源池
    public static final int ERROR_NUM_40201 = 40201;
    public static final String ERROR_40201_ERROR = "网红资源池组数量创建达到上限";

    public static final int ERROR_NUM_40202 = 40202;
    public static final String ERROR_40202_ERROR = "资源池内网红数量达到上限";

    // 网红基本信息
    public static final int ERROR_NUM_40203 = 40203;
    public static final String ERROR_40203_ERROR = "解锁网红基本信息配额不足";

    public static final int ERROR_NUM_40301 = 40301;
    public static final String ERROR_40301_ERROR = "标签不存在";

    public static final int ERROR_NUM_40302 = 40302;
    public static final String ERROR_40302_ERROR = "已存在相同的标签";

    // 定制版高级会员权限
    public static final int ERROR_NUM_93001 = 93001;
    public static final String ERROR_93001_ERROR = "无品牌监控权限-定制版高级用户专有";

    public static final int ERROR_NUM_93002 = 93002;
    public static final String ERROR_93002_ERROR = "无优质MCN查看权限-定制版高级用户专有";

    public static final int ERROR_NUM_93003 = 93003;
    public static final String ERROR_93003_ERROR = "无优质资源搜索权限-定制版高级用户专有";

    public static final int ERROR_NUM_93004 = 93004;
    public static final String ERROR_93004_ERROR = "定制版高级用户专有功能，请联系商务开通";


    public static final int ERROR_NUM_93005 = 93005;
    public static final String ERROR_93005_ERROR = "此MCN不存在";


    public static final int ERROR_NUM_94001 = 94001;
    public static final String ERROR_94001_ERROR = "channelId有误, 并不是优质网红";

    // 外接大鱼致远服务状态澳码
    public static final int ERROR_NUM_60000 = 60000;
    public static final String ERROR_60000_ERROR = "clientId或者secret不匹配";

    public static final int ERROR_NUM_60001 = 60001;
    public static final String ERROR_60001_ERROR = "token刷新失败，请重新请求token";

    public static final int ERROR_NUM_60002 = 60002;
    public static final String ERROR_60002_ERROR = "token验证失败";

    public static final int ERROR_NUM_60003 = 60003;
    public static final String ERROR_60003_ERROR = "账号已开通";

    public static final int ERROR_NUM_60004 = 60004;
    public static final String ERROR_60004_ERROR = "更新会员信息前，请先开通会员";

    public static final int ERROR_NUM_60005 = 60005;
    public static final String ERROR_60005_ERROR = "用户不存在或者用户已注销";

    public static final int ERROR_NUM_60006 = 60006;
    public static final String ERROR_60006_ERROR = "密码解密失败";

    public static final int ERROR_NUM_60007 = 60007;
    public static final String ERROR_60007_ERROR = "idToken验证失败";


    public static final int ERROR_NUM_10005 = 10005;
    public static final String ERROR_MESSAGE_10005_STR = "项目名称异常";

    public static final int ERROR_NUM_10016 = 10016;
    public static final String ERROR_10016_ERROR = "项目名称已经存在";

    public static final int ERROR_NUM_10303 = 10303;
    public static final String ERROR_MESSAGE_10303_STR = "修改监控项目名称失败";

    public static final int ERROR_NUM_20014 = 20014;
    public static final String ERROR_20014_ERROR = "删除监控任务异常";


    public static final int ERROR_NUM_20038 = 20038;
    public static final String ERROR_20038_ERROR = "短链接不能修改，只能添加";

    public static final int ERROR_NUM_20051 = 20051;
    public static final String ERROR_20051_ERROR = "非常规操作";

    public static final int ERROR_NUM_30006 = 30006;
    public static final String ERROR_30006_ERROR = "这个视频已经存在其他任务中，可正常添加";

    public static final int ERROR_NUM_30007 = 30007;
    public static final String ERROR_30007_ERROR = "验证通过";

    public static final int ERROR_NUM_30008 = 30008;
    public static final String ERROR_30008_ERROR = "添加视屏监控任务异常";

    public static final int ERROR_NUM_30009 = 30009;
    public static final String ERROR_30009_ERROR = "全部任务已过期或者未上线";


    public static final int ERROR_NUM_30013 = 30013;
    public static final String ERROR_30013_ERROR = "当前任务中存在此网红和此监控链接";


    public static final int ERROR_NUM_59001 = 59001;
    public static final String ERROR_59001_ERROR = "IM请求响应异常";

    public static final int ERROR_NUM_59002 = 59002;
    public static final String ERROR_59002_ERROR = "IM创建Token异常";

    public static final int ERROR_NUM_59003 = 59003;
    public static final String ERROR_59003_ERROR = "IM刷新Token异常";

    public static final int ERROR_NUM_59004 = 59004;
    public static final String ERROR_59004_ERROR = "IM消息发送失败";

    public static final int ERROR_NUM_59005 = 59005;
    public static final String ERROR_59005_ERROR = "IM历史记录查询失败";

    public static final int ERROR_NUM_59006 = 59006;
    public static final String ERROR_59006_ERROR = "IM生成动态Token异常";

    public static final int ERROR_NUM_59007 = 59007;
    public static final String ERROR_59007_ERROR = "创建IM账号时，uid不存在";

    public static final int ERROR_NUM_59008 = 59008;
    public static final String ERROR_59008_ERROR = "IM账号基本信息刷新失败";

    public static final int ERROR_NUM_59009 = 59009;
    public static final String ERROR_59009_ERROR = "获取授权失败";

    public static final int ERROR_NUM_59010 = 59010;
    public static final String ERROR_59010_ERROR = "APP不支持的登录方式";

    public static final int ERROR_NUM_59011 = 59011;
    public static final String ERROR_59011_ERROR = "广告主不能登录APP";

    public static final int ERROR_NUM_59012 = 59012;
    public static final String ERROR_59012_ERROR = "IM文件上传失败";

    public static final int ERROR_NUM_59013 = 59013;
    public static final String ERROR_59013_ERROR = "IM聊天发送图片失败";

    //广告主代付功能模块
    public static final int ERROR_NUM_70001 = 70001;
    public static final String ERROR_70001_ERROR = "账户余额不足-无法创建支付订单";

    public static final int ERROR_NUM_70002 = 70002;
    public static final String ERROR_70002_ERROR = "订单已处理-无法取消支付";

    public static final int ERROR_NUM_70003 = 70003;
    public static final String ERROR_70003_ERROR = "网红没有查看订单的权限";

    public static final int ERROR_NUM_70005 = 70005;
    public static final String ERROR_70005_ERROR = "请求token出错，请查看原因";

    public static final int ERROR_NUM_70006 = 70006;
    public static final String ERROR_70006_ERROR = "无支付账户实体存在";

    public static final int ERROR_NUM_70007 = 70007;
    public static final String ERROR_70007_ERROR = "支付订单不存在";

    public static final int  ERROR_NUM_70008  = 70008;
    public static final String ERROR_70008_ERROR = "订单状态不匹配";

    public static final int  ERROR_NUM_70009  = 70009;
    public static final String ERROR_70009_ERROR = "网红支付账户状态异常,无法支付";

    public static final int  ERROR_NUM_70013  = 70013;
    public static final String ERROR_70013_ERROR = "请求汇率转化出错";

    public static final int  ERROR_NUM_70016  = 70016;
    public static final String ERROR_70016_ERROR = "验证网红链接出错，无法获得channelId";

    public static final int  ERROR_NUM_70017  = 70017;
    public static final String ERROR_70017_ERROR = "网红未入库，正在处理稍后重试";

    public static final int  ERROR_NUM_70018  = 70018;
    public static final String ERROR_70018_ERROR = "支付金额超出限制";

    public static final int  ERROR_NUM_70019  = 70019;
    public static final String ERROR_70019_ERROR = "提交payonner支付失败，请查看日志";

    public static final int  ERROR_NUM_70020  = 70020;
    public static final String ERROR_70020_ERROR = "网红填充信息状态已改，请刷新";

    public static final int  ERROR_NUM_70021  = 70021;
    public static final String ERROR_70021_ERROR = "合同已保存请勿修改";

    public static final int  ERROR_NUM_70022  = 70022;
    public static final String ERROR_70022_ERROR = "请求payonner注册链接为空,请重试";

    public static final int  ERROR_NUM_70023  = 70023;
    public static final String ERROR_70023_ERROR = "多笔支付,支付比例或者总金额不相等";

    //v571多笔订单支付
    public static final int  ERROR_NUM_70024  = 70024;
    public static final String ERROR_70024_ERROR = "支付计划已经开始支付，无法修改";

    public static final int  ERROR_NUM_70025  = 70025;
    public static final String ERROR_70025_ERROR = "上一笔支付未完成";

    public static final int  ERROR_NUM_70026  = 70026;
    public static final String ERROR_70026_ERROR = "分笔支付开始支付失败";

    public static final int  ERROR_NUM_70027  = 70027;
    public static final String ERROR_70027_ERROR = "推广任务关闭，无法支付";

    public static final int  ERROR_NUM_70028  = 70028;
    public static final String ERROR_70028_ERROR = "已经开始支付，无法修改";

    public static final int  ERROR_NUM_70029  = 70029;
    public static final String ERROR_70029_ERROR = "查询peyonner账户审核状态出错";

    public static final int ERROR_NUM_30100 = 30100;
    public static final String ERROR_30100_ERROR = "没有该推广任务权限";

    //致趣百川对接API异常

    public static final int ERROR_NUM_11001 = 11001;
    public static final String ERROR_11001_ERROR = "build致趣百川token失败";

    public static final int ERROR_NUM_11002 = 11002;
    public static final String ERROR_11002_ERROR = "request zhiqubaichuan service failed";

}
