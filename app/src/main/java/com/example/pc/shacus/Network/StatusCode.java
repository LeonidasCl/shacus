package com.example.pc.shacus.Network;

/**
 * Created by pc on 2016/3/6.
 */
public interface StatusCode {

    int STATUS_ERROR= 0;
    int STATUS_LOGIN=10100;
    int STATUS_REGISTER=10000;

    //请求获取验证码
    int REQUEST_REGISTER_VERIFYA=10001;
    //请求验证验证码
    int REQUEST_REGISTER_VERIFYB=10002;
    //请求注册
    int REQUEST_REGISTER=10003;
    //注册请求类别的成功返回码
    int RECIEVE_REGISTER_SUCCESS=10004;
    //请求登录的请求码
    int REQUEST_LOGIN=10106;
    //登录请求类别成功的返回码
    int REQUEST_LOGIN_SUCCESS =10111 ;
    //请求访问他人个人主页请求码
     int RECIEVE_VISIT_REJECT=10600;
    //获取访问他人个人主页的权限
    int RECIEVE_VISIT_SUCCESS=10601;

    int REQUEST_YUEPAI_SUCCESS=10200;
    int REQUEST_CREATE_YUEPAIA=10201;
    int REQUEST_CREATE_YUEPAIB=10202;
    int REQUEST_CREATE_HUODONG=10301;
    int REQUEST_SEND_YUEPAI=10205;


    int REQUEST_FOLLOW_ERROR = 10419; //服务器错误
    //请求我关注的用户（我的关注）
    int REQUEST_INFO_FOLLOWING = 10403;
    int REQUEST_FOLLOWING_SUCCESS = 10430; //请求我的关注成功
    int REQUEST_FOLLOWING_NONE = 10431; //用户没有关注任何人
    int REQUEST_USER_ILLEGAL = 10412; //用户不合法

    //请求取消关注
    int REQUEST_CANCEL_FOLLOWING = 10402;
    int REQUEST_CANCEL_SUCCESS = 10420; //请求取消关注成功
    int REQUEST_CANCEL_NONE = 10421; //未关注该用户

    //请求关注
    int REQUEST_FOLLOW_USER = 10401;
    int REQUEST_FOLLOW_SUCCESS = 10411; //请求关注成功
    int REQUEST_FOLLOWER_NONE = 10441;//没有粉丝

    //请求我的粉丝
    int REQUEST_INFO_FOLLOWER = 10404;
    int REQUEST_FOLLOWER_SUCCESS = 10440; //请求我的粉丝成功

    int REQUEST_REGIST_ORDER = 10901; //请求用户所有的已报名的约拍和活动
    int REQUEST_ORDER_ERROR = 10391; //请求订单授权码不正确

    int REQUEST_REGIST_SUCCESS = 10392; //请求用户所有的已报名的约拍和活动成功

    int REQUEST_DOING_ORDER = 10902; //请求用户所有的正在进行的约拍和活动
    int REQUEST_DOING_SUCCESS = 10393; //请求进行中成功

    int REQUEST_DONE_ORDER = 10903; //请求用户所有的已完成的约拍和活动
    int REQUEST_DONE_SUCCESS = 10394; //请求已完成成功

    int REQUEST_FAVOR_YUEPAI = 10541; //请求所有收藏的约拍

    int REQUEST_ADD_FAVORYUEPAI = 10501; //请求添加收藏

    int REQUEST_FAVOR_HUODONG = 10502; //请求收藏的活动
    int REQUEST_FAVOR_ZUOPIN = 10503; //请求收藏的作品
    int REQUEST_FAVOR_JIAOCHENG = 10504; //请求收藏的教程
    int REQUEST_CANCEL_FAVORYUEPAI = 10510; //取消收藏约拍
    int REQUEST_CANCELYUEPAI_SUCCESS = 10523;//取消收藏的约拍成功
    int REQUEST_FAVORYUEPAI_NONE = 10526; //用户未收藏任何约拍
    int REQUEST_CANCEL_FAVORHUODONG = 10511; //取消收藏活动
    //int REQUEST_CANCEL_FAVORZUOPIN = 10512; //取消收藏作品
    //int REQUEST_CANCEL_FAVORJIAOCHENG = 10

    int REQUEST_FAVORYUEPAI_SUCCESS = 10550; //请求所有收藏约拍成功
    int REQUEST_OTHERUSER_INFO=10801;
    int REQUEST_HUODONG_SUCCESS=10313;
    int REQUEST_HUODONG_SUCCEED = 10323;
    int REQUEST_SEND_HUODONG=10302;
    int REQUEST_YUEPAI_SUCCEED = 10214;

    int REQUEST_FINISHED_COURSE=11005;//请求用户已完成课程
    int REQUSET_FINISHED_SUCCESS=11051;
    int REQUSET_FINISHED_FAIL=11000;

    int REQUEST_UNDO_COURSE=11006;//请求课程表
    int REQUEST_UNDO_SUCCESS=11061;
    int REQUEST_UNDO_FAIL=11000;

    int REQUEST_KIND_COURSE=11007;//请求分类教程
    int REQUEST_KIND_SECCESS=11071;
    int REQUEST_KIND_FAIL=11000;
    int REQUEST_KIND_NOMORE=11072;

    int REQUEST_DETAIL_COURSE=11002;//请求教程详细信息
    int REQUEST_DETAIL_SECCESS=11021;
    int REQUSET_DETAIL_INVALID=11022;

    int REQUEST_COLLECT_COURSE=11008;//请求收藏课程
    int REQUEST_COLLECT_SUCCESS=11083;
    int REQUEST_COLLECT_ALREADY=11084;
    int REQUEST_SEVER_FAIL=11082;
    int REQUEST_COURSE_INVALID=11081;
    int REQUEST_COLLECT_AUTHKEYWRONG=11080;

    int REQUEST_DISCOLLECT_COURSE=11009;//请求取消收藏
    int REQUEST_DISCOLLECT_SUCCESS=11092;
    int REQUEST_DISCOLLECT_ALREADY=11093;
    int REQUEST_DISCOLLECT_EVER=11091;


    //设置部分请求码
    int REQUEST_SETTING_CHANGE_PASSWORD=10501;
    int REQUEST_SETTING_CHANGE_NICKNAME=10503;
    int REQUEST_SETTING_CHANGE_PHONENUMBER=10505;
    int REQUEST_SETTING_CHANGE_ADDRESS=10507;
    int REQUEST_SETTING_CHANGE_EMAIL=10509;
    int REQUEST_SETTING_CHANGE_PASSWORD_SUCCESS=10501;
    int REQUEST_SETTING_CHANGE_PASSWORD_FAILED=10502;

    int REQUEST_HUODONG_GET_HUODONG=10303;
    int REQUEST_HUODONG_MORE_HUODONG=10304;
    int REQUEST_YUEPAI_DETAIL = 10242;//new
    int REQUEST_DETAIL_SUCCESS=10254;
    int REQUEST_FAILURE = 11111;
    int REQUEST_HUODONG_DETAIL = 10307;
    int REQUEST_JOIN_YUEPAI = 10271;
    int REQUEST_JOIN_YUEPAI_SUCCESS = 10270;
    int REQUEST_CANCEL_YUEPAI = 10275;
    int CANCEL_JOIN_SUCCESS = 10276;
    int REQUEST_JOIN_HUODONG = 10305;
    int REQUEST_CANCEL_HUODONG = 10306;
    int REQUEST_JOIN_HUODONG_SUCCESS = 10352;
    int CANCEL_JOIN_HUODONG_SUCCESS = 10361;
    int REQUEST_HUODONG_DETAIL_SUCCESS = 10371;

    int PRAISE_HUODONG_SUCCESS = 10387;
    int PRAISE_YUEPAI = 10601;
    int PRAISE_YUEPAI_SUCCESS=10600;
    int CANCEL_PRAISE_YUEPAI_SUCCESS = 10615;
    int CANCEL_YUEPAI_PRAISE = 10611;
    int CANCEL_HUODONH_PRAISE = 10362;
    int PRAISE_HUODONG = 10361;

    int PRAISE_HUODONG_CANCEL_SUCCESS = 10386;
    int REQUEST_FAVOURITE_YUEPAI = 10501;
    int REQUEST_FAVOURITE_HUODONG = 10502;
    int REQUEST_FAVOURITE_YUEPAI_SUCCESS = 10520;

    int REQUEST_BAOMING_YUEPAI_USER = 10245;//请求某个约拍报名人列表
    int REQUEST_BAOMING_YUEPAI_USERSUCCESS = 10257; //成功返回选择报名人中报名人列表
    int REQUEST_SELECT_YUEPAIUSER = 10904; //选择约拍对象
    int REQUEST_SELECT_YUEPAIUSER_SUCCESS = 10920; //选择约拍对象成功
    int REUQEST_SELECT_YUEPAIUSER_NONE = 10264; //

    int REQUEST_ALLDONGTAI = 12001; //返回所有发布的动态
    int REQUEST_ALLDONGTAI_SUCCESS = 12013; //请求动态成功
    int REQUESTT_ALLDONGTAI_ERROR = 12012;//用户身份认证失败

    int REQUEST_COURSE_HOMEPAGE_SUCCESS=11010;//获取教程首页
    int REQUEST_COURSE_HOMEPAGE_TAG_SUCCESS=110102;//获取教程首页tag

    int REQUEST_COURSE_MORE_COURSES_SUCCESS=11091;//获取更多课程返回成功

}
