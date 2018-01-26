package com.ider.overlauncher;

import android.os.Environment;

/**
 * Created by Administrator on 2017/3/16.
 * all of  intent to cibn
 */

public class Constant {
    public static final String APK_CACHE_PATH = Environment
            .getExternalStorageDirectory().getPath() + "/launcher/download";
    public static  final String DATA_URL = "http://tv.t002.ottcn.com/i-tvbin/qtv_video/special_channel/get_special_channel?tv_cgi_ver=1.0&format=json&req_from=KK_LAUNCHER&channel_selector=chosen&content_selector=all&pictures=pic_648x364%2bpic_408x230%2bpic_260x364%2bpic_192x108&Q-UA=QV%3D1%26PR%3DVIDEO%26PT%3DKONKA%26CHID%3D10052%26RL%3D1920*1080%26VN%3D3.0.0%26VN_CODE%3D120%26SV%3D4.4.2%26DV%3DMiBOX2%26VN_BUILD%3D0";
    public static final String ADD_PACKAGE = "app.add";
    public static final String ACTION_INSTALLED = "ider.installed.apk";
    public static final String ACTION_SEARCH = "tenvideo2://?action=9";
    public static final String ACTION_LOGIN = "tenvideo2://?action=11";
    public static final String ACTION_HIS = "tenvideo2://?action=10";
    public static final String ACTION_FAV = "tenvideo2://?action=12";

    public static final String ACTION_CHOSEN="tenvideo2://?action=4&tab_id=chosen";
    public static final String ACTION_MOVIE="tenvideo2://?action=3&channel_code=movie";
    public static final String ACTION_CHILDREN="tenvideo2://?action=3&channel_code=children&channel_name=儿童&menu_name=看动画";
    public static final String ACTION_YUESHOW="tenvideo2://?action=3&channel_code=yueshow_video";
    public static final String ACTION_TV="tenvideo2://?action=3&channel_code=tv";
    public static final String ACTION_VARIETY="tenvideo2://?action=3&channel_code=variety";
    public static final String ACTION_PHYSCAL_PAY="tenvideo2://?action=3&channel_code=physical_pay";

    public static final String ACTION_HROCKCHIP = "com.softwinner.TvdFileManager";
    public static final String ACTION_HISADD = "com.tv.history.add";
    public static final String ACTION_HISDELTOAPP = "com.tv..history.del.toapp";
    public static final String ACTION_HISDELTOLAUNCHER = "com.tv.favorite..history.del.tolauncher";

    public static final String ACTION_FAVADD = "com.tv.favorite.add";
    public static final String ACTION_FAVDELTOAPP = "com.tv.favorite.del.toapp";
    public static final String ACTION_FAVDELTOLAUNCHER = "com.tv.favorite.del.tolaunche";

    public static final String PACKAGE_TOOL_CLEAN = "com.ider.clean";
    public static final String PACKAGE_TOOL_WEATHER = "com.ider.weather";
    public static final String PACKAGE_TOOL_FASTKEY = "com.ider.fastkey";
    public static final String PACKAGE_TOOL_APPS = "com.ider.apps";
    public static final String PACKAGE_TOOL_SEARCH = "com.ider.search";
    public static final String PACKAGE_TOOL_HISTORY = "com.ider.history";
    public static final String PACKAGE_TOOL_SPEED = "com.ider.speed";
    public static final String PACKAGE_TOOL_NETSET = "com.ider.netset";
    public static final String PACKAGE_TOOL_DATE = "com.ider.date";
    public static final String PACKAGE_TOOL_DEVELOPE = "com.ider.develope";
    public static final String PACKAGE_TOOL_DISPLAY = "com.ider.display";
    public static final String PACKAGE_TOOL_FACTORY = "com.ider.factory";
    public static final String PACKAGE_TOOL_FLASH = "com.ider.flash";
    public static final String PACKAGE_TOOL_IME = "com.ider.ime";
    public static final String PACKAGE_TOOL_INFO = "com.ider.info";
    public static final String PACKAGE_TOOL_APPMANAGER = "com.ider.appmanager";
    public static final String PACKAGE_TOOL_MORE = "com.ider.more";
    public static final String PACKAGE_TOOL_SOUND = "com.ider.sound";

    public static final String PACKAGE_TOOL_FILE = "com.ider.sound";
    public static final String PACKAGE_TOOL_SETTING = "com.zxy.idersettings";
    public static final String PACKAGE_TOOL_SOFTWINNER = "com.softwinner.TvdFileManager";
    public static final String PACKAGE_TOOL_YZG = "com.ider.yzg";
    // 直播
    public static final String[] tvPackageNames = {
            "com.qclive.tv", "org.jykds.tvlivehd", "com.booslink.Wihome_videoplayer3", "com.cntv.live2",
            "com.elinkway.dianshijia.zhibo", "com.cloudmedia.videoplayer", "com.giec.live", "com.fyzb.tv", "com.kukantv",
            "hdpfans.com", "com.xiaojie.tv", "com.hyh.live", "com.duowan.kiwitv", "cc.zlive.tv", "com.starschina.tv",
            "cooltv.tv", "com.eagle.live", "com.mylove.galaxy", "com.tlmp", "tg.zhibodi.browser2", "com.lovetv.kankan",
            "com.lovetv.ttlive", "com.tinmanarts.TheSuperChallengeTournamentTV", "com.vst.live", "com.linkin.tv",
            "com.gameabc.zhanqiAndroidTv"};
    public static final String[] demandPackageNames = {
            "com.gitvdemo.video", "net.cibntv.ott.sk", "com.pplive.androidxl", "cn.cibn.haibo" , "com.pptv.tvsports" ,
            "com.vcinema.client.tv", "net.myvst.v2" , "com.lesports.tv", "com.haqutv" , "com.vod.dadianying" ,
            "com.iloushu.www.tv" , "com.lovepet" , "com.cibn.tv" , "cn.beevideo" , "com.pptv.tvsports" , "com.konka.voole.video" ,
            "cn.xueximiao.tv" , "com.ktcp.video" , "com.tv.uuvideo" , "com.moretv.android", "com.sohuott.tv.vod", "cn.com.wasu.main" ,
            "com.js.litchi" , "com.starcor.mango" , "com.molitv.android.v2" , "tv.huan.unity" , "com.golive.cinema" ,
            "com.togic.livevideo", "com.letv.tv","com.gitvvideo.yidiankeji"};
    public static final String[] storePackageNames = {
            "com.dangbeimarket" , "tv.beemarket" , "com.shafa.market" , "com.guozi.appstore" , "tv.tv9ikan.app" ,
            "com.linkin.video.search"};



    public static final long START_HELP_TIME = 120000;
}
