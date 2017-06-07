package com.example.pc.shacus.Network;

/**
 * Created by 李嘉文 on 2016/8/6.
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
    //自动登录的请求码
    int REQUEST_AUTO_LOGIN=10105;
    //手动登录的请求码
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
    int RESPONSE_ALREADY_FOLLOW_USER = 10410;
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

    int REQUEST_PJ_DONE_ORDER = 10909;
    int REQUEST_PJ_DONE_SUCCESS = 10395;

    int REQUEST_FAVOR_YUEPAI = 10541; //请求所有收藏的约拍
    int REQUEST_FAVOR_HUODONG = 10542; //请求所有收藏的活动

    int REQUEST_ADD_FAVORYUEPAI = 10501; //请求添加收藏


    int REQUEST_FAVOR_ZUOPIN = 10503; //请求收藏的作品
    int REQUEST_FAVOR_JIAOCHENG = 10504; //请求收藏的教程
    int REQUEST_CANCEL_FAVORYUEPAI = 10510; //取消收藏约拍
    int REQUEST_CANCELYUEPAI_SUCCESS = 10523;//取消收藏的约拍成功
    int REQUEST_FAVORYUEPAI_NONE = 10526; //用户未收藏任何约拍
    int REQUEST_CANCEL_FAVORHUODONG = 10511; //取消收藏活动
    //int REQUEST_CANCEL_FAVORZUOPIN = 10512; //取消收藏作品
    //int REQUEST_CANCEL_FAVORJIAOCHENG = 10

    int REQUEST_CHANGE_HEAD_IMAGE=10513;
    int REQUEST_CHANGE_HEAD_IMAGE_FAIL=10514;
    int REQUEST_CHANGE_HEAD_IMAGE_SUCCESS=10515;
    int REQUEST_SAVE_CHANGED_HEAD_IMAGE=10516;
    int REQUEST_SAVE_CHANGED_HEAD_IMAGE_SUCCESS=66666;

    int REQUEST_FAVORYUEPAI_SUCCESS = 10550; //请求所有收藏约拍成功
    int REQUEST_FAVORHUODONG_SUCCESS = 10551; //请求所有收藏的活动成功

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
    int REQUEST_SETTING_CHANGE_SEX = 10519;
    int REQUEST_SETTING_CHANGE_BIRTH = 10514;
    int REQUEST_SETTING_CHANGE_SIGN=10517;

    int REQUEST_HUODONG_GET_HUODONG=10303;
    int REQUEST_HUODONG_MORE_HUODONG=10304;
    int REQUEST_YUEPAI_DETAIL = 10242;//new
    int REQUEST_YUEPAI_DETAIL_SUCCESS =10254;
    int REQUEST_FAILURE = 11111;
    int REQUEST_HUODONG_DETAIL = 10307;
    int REQUEST_JOIN_YUEPAI = 10271;
    int REQUEST_JOIN_YUEPAI_SUCCESS = 10270;
    int REQUEST_CANCEL_YUEPAI = 10275;
    int CANCEL_JOIN_YUEPAI_SUCCESS = 10276;
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
    int REQUEST_FAVOR_DONGTAI = 12004; //请求所有收藏的动态
    int REQUEST_FAVOR_DONGTAI_SUCCESS = 12401; //请求所有收藏的动态成功
    int REQUEST_FAVOR_DONGTAI_NONE = 12402; //没有收藏的动态
    int REQUEST_ADD_FAVORDONGTAI = 12002; //收藏动态
    int REQUEST_CANCEL_FAVORDONGTAI = 12003; //取消收藏动态
    int REQUEST_CANCEL_FAVORDONGTAI_SUCCESS = 12301;//取消收藏成功
    int REQUEST_ADD_FAVORDONGTAI_SUCCESS = 12021;//添加收藏成功
    int REQUEST_FAVORDONGTAI_SUCCESS = 00001;//请求所有收藏动态成功

    int REQUEST_COURSE_HOMEPAGE_SUCCESS=11010;//获取教程首页
    int REQUEST_COURSE_HOMEPAGE_TAG_SUCCESS=110102;//获取教程首页tag

    int REQUEST_HUODONGLIST_SUCCESS = 10371; //活动报名人返回成功
    int REQUEST_COURSE_MORE_COURSES=11101;
    int REQUEST_COURSE_MORE_COURSES_SUCCESS=11110;//获取更多课程返回成功


    int REQUEST_YUEPAI_GRAPH_LIST=10231;//获得所有摄影师发布的未关闭的最新6个约拍
    int REQUEST_YUEPAI_MODEL_LIST=10235;//获得所有模特发布的未关闭的最新6个约拍
    int REQUEST_YUEPAI_MORE_GRAPH_LIST=10243;//拿到更多6个摄影师发布的约拍 (new)
    int REQUEST_YUEPAI_MORE_MODEL_LIST=10244;//拿到更多6个模特发布的约拍(new)
    int REQUEST_YUEPAI_MODEL_LIST_SUCCESS=10252;//成功
    int REQUEST_YUEPAI_GRAPH_LIST_SUCCESS=10251;
    int REQUEST_YUEPAI_MORE_GRAPH_LIST_SUCCESS=102532;
    int REQUEST_YUEPAI_MORE_MODEL_LIST_SUCCESS=102533;

    int REQUEST_YUEPAI_COMMAND_ERROR=10260;//请求错误
    int YUEPAI_NO_MORE_LIST=10262;//没有更多约拍
    int YUEPAI_NO_LIST=10261;//没有找到约拍记录

    int CANCEL_HUODONG = 10307;
    int REQUEST_CANCEL_HUODONG_SUCCESS = 10325;
    int CANCEL_YUEPAI = 10207;
    int REQUEST_CANCEL_YUEPAI_SUCCESS=10200;
    int REQUEST_FINISH_YUEPAI = 10905;
    int REQUEST_FINISH_YUEPAI_SUCCESS = 10258;
    int REQUEST_FINISH_HUODONG = 10906;
    int REQUEST_FINISH_HUODONG_SUCCESS = 10962;
    int REQUEST_FINISH_JOIN_HUODONG = 10907;
    int REQUEST_FINISH_JOIN_HUODONG_SUCCESS=10972;
    int GET_OTHER_USER_INFO = 10705;
    int REQUEST_OTHER_USER_SUCCESS = 10706;
    int REQUEST_OTHER_INFORMATION = 10701;//在聊天页面中请求他人昵称

    int REQUEST_PAIHANG_PHOTOGRAPHER=10281;//摄影师排行
    int REQUEST_PAIHANG_MODEL=10282;//模特排行
    int REQUEST_PAIHANG_SUCCESS=10285;
    int REQUEST_PAIHANG_FAIL=10286;

    int PHOTOSELF_SMALLIMG = 10814;
    int PHOTOSELF_BIGIMG = 10812;
    int PHOTOSET_SMALLIMG = 10816;//作品集详情获取
    int PHOTOSET_BIGIMG = 10818;//作品集列表获取
    int PHOTOSELF_ADD_IMGS_1 = 10808;//个人照片添加第一步
    int UPDATE_DELETE_SELFIMG = 10811;//个人照片的删除
    int UPDATE_DELETE_SETIMG = 10820;//作品集内图片的删除
    int PHOTOSELF_ADD_IMGS_2 = 10810;//个人照片添加第二步，与个人照片更新请求是一样的
    int PHOTOSET_ADD_SETS_1 = 10804;//添加作品集第一步
    int PHOTOSET_ADD_SETS_2 = 10806;//添加作品集第二步
    int PHOTOSET_ADD_IMGS_1 = 10822;//给作品集添加图片第一步
    int PHOTOSET_ADD_IMGS_2 = 10824;//给作品集添加图片第二步
    int UPDATE_DELETE_PHOTOSET = 10826;//删除作品集
    int REQUEST_USER_GRZP = 10812;// 个人照片
    int REQUEST_USER_ZPJ = 10828;//作品集

    int WANT_TO_PHOTOGRAPH=10231;//获取列表-摄影师发布的约拍
    int WANT_BE_PHOTOGRAPH=10235;//获取列表-模特发布的约拍
    int WANT_TO_PHOTOGRAPH_MORE=10243;//获取指定id后更多-摄影师发布的约拍
    int WANT_BE_PHOTOGRAPH_MORE=10244;//获取指定id后更多-模特发布的约拍

    int REQUEST_FAVOR_PHOTOSET_LIST = 10830;//请求好友的作品集
    int REQUESTRECOMMENDED_PHOTOSET_LIST = 10834;//请求推荐的作品集
    int RETURN_FAVOR_PHOTOSET_LIST = 10832;//返回码-请求更多好友的作品集
    int REQUEST_MORE_FAVOR_PHOTOSET_LIST = 10832;//请求更多好友的作品集
    int RETURN_RECOMMENDED_PHOTOSET_LIST = 10836;//返回码-请求更多推荐的作品集
    int REQUEST_MORE_RECOMMENDED_PHOTOSET_LIST = 10836;//请求更多推荐的作品集

    int PRAISE_PHOTOSET = 10840;//作品集点赞
    int CANCEL_PRAISE_PHOTOSET=10841;//作品集取消点赞
    int RECOMMAND_PHOTOGRAPHER_MODEL_LIST = 10850;//推荐摄影师和模特列表
    int RETURN_SET_CATEGORY = 10851;//服务器返回设置摄影师类型
    int CHANGE_USER_CATEGORY = 10852;//修改用户类型是摄影师还是模特
    int PRAISE_PHOTOSET_SUCCESS = 10842;//作品集点赞成功
    int CANCEL_PRAISE_PHOTOSET_SUCCESS = 10843;//作品集取消赞成功

    int REQUEST_FORGOTPW = 12001;
    int REQUEST_FORGOTPW_ERROR = 12002;
    int REQUEST_FORGOTPW_NONE = 12003;

    int REQUEST_FORGOTPW_YZ = 12004;
    int REQUEST_FORGOTPW_YZ_ERROR = 12005;

    int REQUEST_FORGOTPW_SET = 12007;
    int REQUEST_FORGOTPW_SET_ERROR = 12008;

}
