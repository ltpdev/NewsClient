package com.gdcp.newsclient.bean;

import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by asus- on 2017/8/6.
 */

public class FilmBean {

    /**
     * expires : 1800
     */

    private ControlBean control;
    /**
     * control : {"expires":1800}
     * status : 0
     * data : {"hasNext":true,"movies":[{"showInfo":"今天42家影院放映938场","late":false,"sn":0,"cnms":0,"dur":123,"pn":512,"preSale":0,"vd":"","dir":"吴京","star":"吴京,弗兰克·格里罗,吴刚","cat":"动作,战争","wish":400928,"3d":true,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/02ac72c0e8ee2987f7662ad921a2acc7999433.jpg","sc":9.7,"ver":"2D/3D/中国巨幕/全景声","rt":"2017-07-27上映","imax":false,"snum":1543886,"nm":"战狼2","scm":"非洲战强敌，坦克玩漂移","id":344264,"time":""},{"showInfo":"今天42家影院放映321场","late":false,"sn":0,"cnms":0,"dur":109,"pn":235,"preSale":0,"vd":"","dir":"赵小丁,安东尼.拉默里纳拉","star":"刘亦菲,杨洋,罗晋","cat":"爱情,奇幻,古装","wish":811385,"3d":true,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/e5a715b3fcbd9e928dc96aefa6b19d58379838.jpg","sc":7.1,"ver":"3D/IMAX 3D/中国巨幕/全景声","rt":"2017-08-03上映","imax":true,"snum":293540,"nm":"三生三世十里桃花","scm":"虐心姐弟恋，颜值要逆天","id":246896,"time":""},{"showInfo":"2017-08-11 下周五上映","late":false,"sn":0,"cnms":0,"dur":108,"pn":156,"preSale":1,"vd":"","dir":"冯德伦","star":"刘德华,舒淇,张静初","cat":"动作,冒险","wish":86512,"3d":false,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/26ca66778a40ef2bbcfd33877017a84e1033521.jpg","sc":0,"ver":"2D/IMAX 2D/中国巨幕","rt":"本周五上映","imax":true,"snum":2065,"nm":"侠盗联盟","scm":"侠盗三剑客，越洋逃恐吓","id":1183619,"time":""},{"showInfo":"2017-08-11 下周五上映","late":false,"sn":0,"cnms":0,"dur":108,"pn":170,"preSale":1,"vd":"","dir":"杨磊","star":"王大陆,张天爱,盛冠森","cat":"动作,喜剧,奇幻","wish":110126,"3d":true,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/a229a1429ea8209e19c2cc24d0cc8c4a994102.png","sc":0,"ver":"2D/3D/IMAX 3D/中国巨幕","rt":"本周五上映","imax":true,"snum":3190,"nm":"鲛珠传","scm":"改编热IP，杠杠号召力","id":245938,"time":""},{"showInfo":"2017-08-11 下周五上映","late":false,"sn":0,"cnms":0,"dur":110,"pn":254,"preSale":1,"vd":"","dir":"谢东燊","star":"李易峰,廖凡,万茜","cat":"动作,悬疑,犯罪","wish":139340,"3d":false,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/1068ddf67b070ced8a39fac277929b0e980155.png","sc":9.8,"ver":"2D","rt":"本周五上映","imax":false,"snum":277,"nm":"心理罪","scm":"绝妙两神探，侵心破奇案","id":342381,"time":""},{"showInfo":"今天28家影院放映55场","late":false,"sn":0,"cnms":0,"dur":133,"pn":391,"preSale":0,"vd":"","dir":"刘伟强","star":"刘烨,朱亚文,黄志忠","cat":"剧情,历史","wish":99343,"3d":false,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/0e3a853a93d342f5d071f5e0867cbfb7518640.jpg","sc":9.1,"ver":"2D/IMAX 2D/中国巨幕","rt":"2017-07-27上映","imax":true,"snum":176085,"nm":"建军大业","scm":"群星敬功勋，热血献我军","id":345011,"time":""},{"showInfo":"今天24家影院放映46场","late":false,"sn":0,"cnms":0,"dur":88,"pn":66,"preSale":0,"vd":"","dir":"申宇,黄燕","star":"严丽祯,李晔,王衡","cat":"动画,冒险,奇幻","wish":15345,"3d":true,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/170ada27b8435fb46e0bee6c50fc1efe181196.jpg","sc":8.6,"ver":"2D/3D","rt":"2017-08-04上映","imax":false,"snum":4815,"nm":"玩偶奇兵","scm":"玩偶伴童心，游戏莫沉迷","id":343470,"time":""},{"showInfo":"今天22家影院放映37场","late":false,"sn":0,"cnms":0,"dur":90,"pn":156,"preSale":0,"vd":"","dir":"皮艾尔·柯芬,凯尔·巴尔达","star":"史蒂夫·卡瑞尔,克里斯汀·韦格,崔·帕克","cat":"喜剧,动画,冒险","wish":250810,"3d":true,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/e4aa7abb6ef5fbe695174063ac6cc0921246361.jpg","sc":8.8,"ver":"3D/IMAX 3D/中国巨幕","rt":"2017-07-07上映","imax":true,"snum":385343,"nm":"神偷奶爸3","scm":"婚后也不易，偶遇胞兄弟","id":249900,"time":""},{"showInfo":"2017-08-17上映","late":false,"sn":0,"cnms":0,"dur":101,"pn":125,"preSale":1,"vd":"","dir":"叶伟信","star":"古天乐,托尼·贾,吴樾","cat":"剧情,动作,犯罪","wish":126679,"3d":false,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/5097ccd2d26924642161e3b101943c31304962.jpg","sc":0,"ver":"2D","rt":"下周四上映","imax":false,"snum":1428,"nm":"杀破狼·贪狼","scm":"三男一台戏，主角还是谜","id":342176,"time":""},{"showInfo":"今天12家影院放映21场","late":false,"sn":0,"cnms":0,"dur":110,"pn":205,"preSale":0,"vd":"","dir":"杨子","star":"斯蒂芬·马布里,何冰,吴尊","cat":"剧情,运动","wish":29779,"3d":false,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/34d686ed8a01f561ebb38ff2d702efec749525.png","sc":8.9,"ver":"2D","rt":"2017-08-04上映","imax":false,"snum":4583,"nm":"我是马布里","scm":"吴尊助冠军，热血灌篮魂","id":248648,"time":""},{"showInfo":"今天7家影院放映13场","late":false,"sn":0,"cnms":0,"dur":91,"pn":138,"preSale":0,"vd":"","dir":"速达,李晔","star":"豆豆,范楚绒,胡谦","cat":"喜剧,动画,冒险","wish":19165,"3d":false,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/1eb7dc53464f846cf74e667bb4b7f298422855.jpg","sc":8.7,"ver":"2D","rt":"2017-07-28上映","imax":false,"snum":18044,"nm":"大耳朵图图之美食狂想曲","scm":"图图变厨神，萌娃变勤奋","id":345972,"time":""},{"showInfo":"今天10家影院放映10场","late":false,"sn":0,"cnms":0,"dur":89,"pn":17,"preSale":0,"vd":"","dir":"常杨","star":"彭禺厶,王萌,周凯文","cat":"恐怖,惊悚,悬疑","wish":2001,"3d":false,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/dbd184dfd1845a6e3222aebc0f4873ff935626.png","sc":4.1,"ver":"2D","rt":"2017-08-04上映","imax":false,"snum":357,"nm":"诡井","scm":"午夜深井中，怨魂欲现形","id":1207975,"time":""},{"showInfo":"今天7家影院放映9场","late":false,"sn":0,"cnms":0,"dur":90,"pn":54,"preSale":1,"vd":"","dir":"王章俊","star":"罗玉婷,翟巍,王晓彤","cat":"喜剧,动画,冒险","wish":19753,"3d":true,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/f37aabd3ac27a85fd5ff5e59afb948ab1916084.png","sc":8.9,"ver":"2D/3D","rt":"下周五上映","imax":false,"snum":543,"nm":"赛尔号大电影6：圣者无敌","scm":"船员又相聚，齐心赢胜利","id":1201161,"time":""},{"showInfo":"今天2家影院放映5场","late":false,"sn":0,"cnms":0,"dur":103,"pn":180,"preSale":0,"vd":"","dir":"王冉","star":"徐璐,彭昱畅,刘泳希","cat":"剧情,喜剧,音乐","wish":14489,"3d":false,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/0002a0c15991847aba14169bc19e6e375165298.jpg","sc":9.2,"ver":"2D","rt":"2017-07-20上映","imax":false,"snum":54479,"nm":"闪光少女","scm":"奇葩恋校草，联合大逆袭","id":1132316,"time":""},{"showInfo":"今天2家影院放映4场","late":false,"sn":0,"cnms":0,"dur":82,"pn":102,"preSale":0,"vd":"","dir":"邹燚","star":"陈佩斯,李立群,季冠霖","cat":"喜剧,动画,奇幻","wish":12598,"3d":true,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/6231e053be03367131db94295902183d1178489.jpg","sc":7.9,"ver":"2D/3D/中国巨幕","rt":"2017-07-28上映","imax":false,"snum":12874,"nm":"豆福传","scm":"只要功夫深，豆子变上神","id":1204581,"time":""},{"showInfo":"今天3家影院放映3场","late":false,"sn":0,"cnms":0,"dur":77,"pn":69,"preSale":0,"vd":"","dir":"李涛歌","star":"宋晓宇,李垚,徐炜","cat":"喜剧,动画,战争","wish":2843,"3d":false,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/11e3d28aa1e6ad7a278b6dbefda965e9605865.jpg","sc":9,"ver":"2D","rt":"2017-08-04上映","imax":false,"snum":1387,"nm":"大象林旺之一炮成名","scm":"大象参二战，一生好伙伴","id":345063,"time":""},{"showInfo":"今天2家影院放映2场","late":false,"sn":0,"cnms":0,"dur":99,"pn":272,"preSale":0,"vd":"","dir":"王微","star":"石磊,袁泽宇,芒来","cat":"动画,冒险,家庭","wish":15520,"3d":true,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/e7415fcfaac2f50a755fe38dd6d9894d937103.jpg","sc":8.9,"ver":"2D/3D","rt":"2017-07-21上映","imax":false,"snum":16228,"nm":"阿唐奇遇","scm":"茶店丑茶宠，为美大步走","id":367815,"time":""},{"showInfo":"今天2家影院放映2场","late":false,"sn":0,"cnms":0,"dur":112,"pn":95,"preSale":0,"vd":"","dir":"高峰","star":"刘佩琦,曹云金,罗昱焜","cat":"动作,战争,历史","wish":11990,"3d":false,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/2bad4d79ed1d2ca6768ec6d6bdd97c5e588735.jpg","sc":8.7,"ver":"2D","rt":"2017-08-04上映","imax":false,"snum":1739,"nm":"龙之战","scm":"持倭刀屹立，抗外敌救国","id":1181801,"time":""},{"showInfo":"今天1家影院放映1场","late":false,"sn":0,"cnms":0,"dur":92,"pn":29,"preSale":0,"vd":"","dir":"Fyodor Dmitriev,Darina Shmidt,Vladimir Toropchin","star":"Dmitriy Dyuzhev,Konstantin Khabenskiy,Sergey Shnurov","cat":"动画,冒险,家庭","wish":5590,"3d":true,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/834e088a4cb8606aee52d25eab387cad2723458.jpg","sc":8.2,"ver":"2D/3D","rt":"2017-07-21上映","imax":false,"snum":2952,"nm":"绿野仙踪之奥兹国奇幻之旅","scm":"智慧胜邪恶，拯救奥兹国","id":1205027,"time":""},{"showInfo":"今天1家影院放映1场","late":false,"sn":0,"cnms":0,"dur":92,"pn":27,"preSale":0,"vd":"","dir":"李耀东","star":"刘青,李浩轩,白瑶","cat":"恐怖,悬疑","wish":2815,"3d":false,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/5d9c50d8f3a6b230ad72bf9b008fc99268765.jpg","sc":3,"ver":"2D","rt":"2017-07-28上映","imax":false,"snum":2931,"nm":"夜半凶铃","scm":"玩神秘游戏，步危险迷局","id":1207706,"time":""}]}
     */

    private int status;
    /**
     * hasNext : true
     * movies : [{"showInfo":"今天42家影院放映938场","late":false,"sn":0,"cnms":0,"dur":123,"pn":512,"preSale":0,"vd":"","dir":"吴京","star":"吴京,弗兰克·格里罗,吴刚","cat":"动作,战争","wish":400928,"3d":true,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/02ac72c0e8ee2987f7662ad921a2acc7999433.jpg","sc":9.7,"ver":"2D/3D/中国巨幕/全景声","rt":"2017-07-27上映","imax":false,"snum":1543886,"nm":"战狼2","scm":"非洲战强敌，坦克玩漂移","id":344264,"time":""},{"showInfo":"今天42家影院放映321场","late":false,"sn":0,"cnms":0,"dur":109,"pn":235,"preSale":0,"vd":"","dir":"赵小丁,安东尼.拉默里纳拉","star":"刘亦菲,杨洋,罗晋","cat":"爱情,奇幻,古装","wish":811385,"3d":true,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/e5a715b3fcbd9e928dc96aefa6b19d58379838.jpg","sc":7.1,"ver":"3D/IMAX 3D/中国巨幕/全景声","rt":"2017-08-03上映","imax":true,"snum":293540,"nm":"三生三世十里桃花","scm":"虐心姐弟恋，颜值要逆天","id":246896,"time":""},{"showInfo":"2017-08-11 下周五上映","late":false,"sn":0,"cnms":0,"dur":108,"pn":156,"preSale":1,"vd":"","dir":"冯德伦","star":"刘德华,舒淇,张静初","cat":"动作,冒险","wish":86512,"3d":false,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/26ca66778a40ef2bbcfd33877017a84e1033521.jpg","sc":0,"ver":"2D/IMAX 2D/中国巨幕","rt":"本周五上映","imax":true,"snum":2065,"nm":"侠盗联盟","scm":"侠盗三剑客，越洋逃恐吓","id":1183619,"time":""},{"showInfo":"2017-08-11 下周五上映","late":false,"sn":0,"cnms":0,"dur":108,"pn":170,"preSale":1,"vd":"","dir":"杨磊","star":"王大陆,张天爱,盛冠森","cat":"动作,喜剧,奇幻","wish":110126,"3d":true,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/a229a1429ea8209e19c2cc24d0cc8c4a994102.png","sc":0,"ver":"2D/3D/IMAX 3D/中国巨幕","rt":"本周五上映","imax":true,"snum":3190,"nm":"鲛珠传","scm":"改编热IP，杠杠号召力","id":245938,"time":""},{"showInfo":"2017-08-11 下周五上映","late":false,"sn":0,"cnms":0,"dur":110,"pn":254,"preSale":1,"vd":"","dir":"谢东燊","star":"李易峰,廖凡,万茜","cat":"动作,悬疑,犯罪","wish":139340,"3d":false,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/1068ddf67b070ced8a39fac277929b0e980155.png","sc":9.8,"ver":"2D","rt":"本周五上映","imax":false,"snum":277,"nm":"心理罪","scm":"绝妙两神探，侵心破奇案","id":342381,"time":""},{"showInfo":"今天28家影院放映55场","late":false,"sn":0,"cnms":0,"dur":133,"pn":391,"preSale":0,"vd":"","dir":"刘伟强","star":"刘烨,朱亚文,黄志忠","cat":"剧情,历史","wish":99343,"3d":false,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/0e3a853a93d342f5d071f5e0867cbfb7518640.jpg","sc":9.1,"ver":"2D/IMAX 2D/中国巨幕","rt":"2017-07-27上映","imax":true,"snum":176085,"nm":"建军大业","scm":"群星敬功勋，热血献我军","id":345011,"time":""},{"showInfo":"今天24家影院放映46场","late":false,"sn":0,"cnms":0,"dur":88,"pn":66,"preSale":0,"vd":"","dir":"申宇,黄燕","star":"严丽祯,李晔,王衡","cat":"动画,冒险,奇幻","wish":15345,"3d":true,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/170ada27b8435fb46e0bee6c50fc1efe181196.jpg","sc":8.6,"ver":"2D/3D","rt":"2017-08-04上映","imax":false,"snum":4815,"nm":"玩偶奇兵","scm":"玩偶伴童心，游戏莫沉迷","id":343470,"time":""},{"showInfo":"今天22家影院放映37场","late":false,"sn":0,"cnms":0,"dur":90,"pn":156,"preSale":0,"vd":"","dir":"皮艾尔·柯芬,凯尔·巴尔达","star":"史蒂夫·卡瑞尔,克里斯汀·韦格,崔·帕克","cat":"喜剧,动画,冒险","wish":250810,"3d":true,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/e4aa7abb6ef5fbe695174063ac6cc0921246361.jpg","sc":8.8,"ver":"3D/IMAX 3D/中国巨幕","rt":"2017-07-07上映","imax":true,"snum":385343,"nm":"神偷奶爸3","scm":"婚后也不易，偶遇胞兄弟","id":249900,"time":""},{"showInfo":"2017-08-17上映","late":false,"sn":0,"cnms":0,"dur":101,"pn":125,"preSale":1,"vd":"","dir":"叶伟信","star":"古天乐,托尼·贾,吴樾","cat":"剧情,动作,犯罪","wish":126679,"3d":false,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/5097ccd2d26924642161e3b101943c31304962.jpg","sc":0,"ver":"2D","rt":"下周四上映","imax":false,"snum":1428,"nm":"杀破狼·贪狼","scm":"三男一台戏，主角还是谜","id":342176,"time":""},{"showInfo":"今天12家影院放映21场","late":false,"sn":0,"cnms":0,"dur":110,"pn":205,"preSale":0,"vd":"","dir":"杨子","star":"斯蒂芬·马布里,何冰,吴尊","cat":"剧情,运动","wish":29779,"3d":false,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/34d686ed8a01f561ebb38ff2d702efec749525.png","sc":8.9,"ver":"2D","rt":"2017-08-04上映","imax":false,"snum":4583,"nm":"我是马布里","scm":"吴尊助冠军，热血灌篮魂","id":248648,"time":""},{"showInfo":"今天7家影院放映13场","late":false,"sn":0,"cnms":0,"dur":91,"pn":138,"preSale":0,"vd":"","dir":"速达,李晔","star":"豆豆,范楚绒,胡谦","cat":"喜剧,动画,冒险","wish":19165,"3d":false,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/1eb7dc53464f846cf74e667bb4b7f298422855.jpg","sc":8.7,"ver":"2D","rt":"2017-07-28上映","imax":false,"snum":18044,"nm":"大耳朵图图之美食狂想曲","scm":"图图变厨神，萌娃变勤奋","id":345972,"time":""},{"showInfo":"今天10家影院放映10场","late":false,"sn":0,"cnms":0,"dur":89,"pn":17,"preSale":0,"vd":"","dir":"常杨","star":"彭禺厶,王萌,周凯文","cat":"恐怖,惊悚,悬疑","wish":2001,"3d":false,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/dbd184dfd1845a6e3222aebc0f4873ff935626.png","sc":4.1,"ver":"2D","rt":"2017-08-04上映","imax":false,"snum":357,"nm":"诡井","scm":"午夜深井中，怨魂欲现形","id":1207975,"time":""},{"showInfo":"今天7家影院放映9场","late":false,"sn":0,"cnms":0,"dur":90,"pn":54,"preSale":1,"vd":"","dir":"王章俊","star":"罗玉婷,翟巍,王晓彤","cat":"喜剧,动画,冒险","wish":19753,"3d":true,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/f37aabd3ac27a85fd5ff5e59afb948ab1916084.png","sc":8.9,"ver":"2D/3D","rt":"下周五上映","imax":false,"snum":543,"nm":"赛尔号大电影6：圣者无敌","scm":"船员又相聚，齐心赢胜利","id":1201161,"time":""},{"showInfo":"今天2家影院放映5场","late":false,"sn":0,"cnms":0,"dur":103,"pn":180,"preSale":0,"vd":"","dir":"王冉","star":"徐璐,彭昱畅,刘泳希","cat":"剧情,喜剧,音乐","wish":14489,"3d":false,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/0002a0c15991847aba14169bc19e6e375165298.jpg","sc":9.2,"ver":"2D","rt":"2017-07-20上映","imax":false,"snum":54479,"nm":"闪光少女","scm":"奇葩恋校草，联合大逆袭","id":1132316,"time":""},{"showInfo":"今天2家影院放映4场","late":false,"sn":0,"cnms":0,"dur":82,"pn":102,"preSale":0,"vd":"","dir":"邹燚","star":"陈佩斯,李立群,季冠霖","cat":"喜剧,动画,奇幻","wish":12598,"3d":true,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/6231e053be03367131db94295902183d1178489.jpg","sc":7.9,"ver":"2D/3D/中国巨幕","rt":"2017-07-28上映","imax":false,"snum":12874,"nm":"豆福传","scm":"只要功夫深，豆子变上神","id":1204581,"time":""},{"showInfo":"今天3家影院放映3场","late":false,"sn":0,"cnms":0,"dur":77,"pn":69,"preSale":0,"vd":"","dir":"李涛歌","star":"宋晓宇,李垚,徐炜","cat":"喜剧,动画,战争","wish":2843,"3d":false,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/11e3d28aa1e6ad7a278b6dbefda965e9605865.jpg","sc":9,"ver":"2D","rt":"2017-08-04上映","imax":false,"snum":1387,"nm":"大象林旺之一炮成名","scm":"大象参二战，一生好伙伴","id":345063,"time":""},{"showInfo":"今天2家影院放映2场","late":false,"sn":0,"cnms":0,"dur":99,"pn":272,"preSale":0,"vd":"","dir":"王微","star":"石磊,袁泽宇,芒来","cat":"动画,冒险,家庭","wish":15520,"3d":true,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/e7415fcfaac2f50a755fe38dd6d9894d937103.jpg","sc":8.9,"ver":"2D/3D","rt":"2017-07-21上映","imax":false,"snum":16228,"nm":"阿唐奇遇","scm":"茶店丑茶宠，为美大步走","id":367815,"time":""},{"showInfo":"今天2家影院放映2场","late":false,"sn":0,"cnms":0,"dur":112,"pn":95,"preSale":0,"vd":"","dir":"高峰","star":"刘佩琦,曹云金,罗昱焜","cat":"动作,战争,历史","wish":11990,"3d":false,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/2bad4d79ed1d2ca6768ec6d6bdd97c5e588735.jpg","sc":8.7,"ver":"2D","rt":"2017-08-04上映","imax":false,"snum":1739,"nm":"龙之战","scm":"持倭刀屹立，抗外敌救国","id":1181801,"time":""},{"showInfo":"今天1家影院放映1场","late":false,"sn":0,"cnms":0,"dur":92,"pn":29,"preSale":0,"vd":"","dir":"Fyodor Dmitriev,Darina Shmidt,Vladimir Toropchin","star":"Dmitriy Dyuzhev,Konstantin Khabenskiy,Sergey Shnurov","cat":"动画,冒险,家庭","wish":5590,"3d":true,"showDate":"","src":"","img":"http://p1.meituan.net/165.220/movie/834e088a4cb8606aee52d25eab387cad2723458.jpg","sc":8.2,"ver":"2D/3D","rt":"2017-07-21上映","imax":false,"snum":2952,"nm":"绿野仙踪之奥兹国奇幻之旅","scm":"智慧胜邪恶，拯救奥兹国","id":1205027,"time":""},{"showInfo":"今天1家影院放映1场","late":false,"sn":0,"cnms":0,"dur":92,"pn":27,"preSale":0,"vd":"","dir":"李耀东","star":"刘青,李浩轩,白瑶","cat":"恐怖,悬疑","wish":2815,"3d":false,"showDate":"","src":"","img":"http://p0.meituan.net/165.220/movie/5d9c50d8f3a6b230ad72bf9b008fc99268765.jpg","sc":3,"ver":"2D","rt":"2017-07-28上映","imax":false,"snum":2931,"nm":"夜半凶铃","scm":"玩神秘游戏，步危险迷局","id":1207706,"time":""}]
     */

    private DataBean data;

    public ControlBean getControl() {
        return control;
    }

    public void setControl(ControlBean control) {
        this.control = control;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class ControlBean {
        private int expires;

        public int getExpires() {
            return expires;
        }

        public void setExpires(int expires) {
            this.expires = expires;
        }
    }

    public static class DataBean {
        private boolean hasNext;
        /**
         * showInfo : 今天42家影院放映938场
         * late : false
         * sn : 0
         * cnms : 0
         * dur : 123
         * pn : 512
         * preSale : 0
         * vd :
         * dir : 吴京
         * star : 吴京,弗兰克·格里罗,吴刚
         * cat : 动作,战争
         * wish : 400928
         * 3d : true
         * showDate :
         * src :
         * img : http://p0.meituan.net/165.220/movie/02ac72c0e8ee2987f7662ad921a2acc7999433.jpg
         * sc : 9.7
         * ver : 2D/3D/中国巨幕/全景声
         * rt : 2017-07-27上映
         * imax : false
         * snum : 1543886
         * nm : 战狼2
         * scm : 非洲战强敌，坦克玩漂移
         * id : 344264
         * time :
         */

        private List<MoviesBean> movies;

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public List<MoviesBean> getMovies() {
            return movies;
        }

        public void setMovies(List<MoviesBean> movies) {
            this.movies = movies;
        }

        public static class MoviesBean {
            private String showInfo;
            private boolean late;
            private int sn;
            private int cnms;
            private int dur;
            private int pn;
            private int preSale;
            private String vd;
            private String dir;
            private String star;
            private String cat;
            private int wish;
            @SerializedName("3d")
            private boolean value3d;
            private String showDate;
            private String src;
            private String img;
            private double sc;
            private String ver;
            private String rt;
            private boolean imax;
            private int snum;
            private String nm;
            private String scm;
            private int id;
            private String time;

            public String getShowInfo() {
                return showInfo;
            }

            public void setShowInfo(String showInfo) {
                this.showInfo = showInfo;
            }

            public boolean isLate() {
                return late;
            }

            public void setLate(boolean late) {
                this.late = late;
            }

            public int getSn() {
                return sn;
            }

            public void setSn(int sn) {
                this.sn = sn;
            }

            public int getCnms() {
                return cnms;
            }

            public void setCnms(int cnms) {
                this.cnms = cnms;
            }

            public int getDur() {
                return dur;
            }

            public void setDur(int dur) {
                this.dur = dur;
            }

            public int getPn() {
                return pn;
            }

            public void setPn(int pn) {
                this.pn = pn;
            }

            public int getPreSale() {
                return preSale;
            }

            public void setPreSale(int preSale) {
                this.preSale = preSale;
            }

            public String getVd() {
                return vd;
            }

            public void setVd(String vd) {
                this.vd = vd;
            }

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getStar() {
                return star;
            }

            public void setStar(String star) {
                this.star = star;
            }

            public String getCat() {
                return cat;
            }

            public void setCat(String cat) {
                this.cat = cat;
            }

            public int getWish() {
                return wish;
            }

            public void setWish(int wish) {
                this.wish = wish;
            }

            public boolean isValue3d() {
                return value3d;
            }

            public void setValue3d(boolean value3d) {
                this.value3d = value3d;
            }

            public String getShowDate() {
                return showDate;
            }

            public void setShowDate(String showDate) {
                this.showDate = showDate;
            }

            public String getSrc() {
                return src;
            }

            public void setSrc(String src) {
                this.src = src;
            }

            public String getImg() {
                return img;
            }

            public void setImg(String img) {
                this.img = img;
            }

            public double getSc() {
                return sc;
            }

            public void setSc(double sc) {
                this.sc = sc;
            }

            public String getVer() {
                return ver;
            }

            public void setVer(String ver) {
                this.ver = ver;
            }

            public String getRt() {
                return rt;
            }

            public void setRt(String rt) {
                this.rt = rt;
            }

            public boolean isImax() {
                return imax;
            }

            public void setImax(boolean imax) {
                this.imax = imax;
            }

            public int getSnum() {
                return snum;
            }

            public void setSnum(int snum) {
                this.snum = snum;
            }

            public String getNm() {
                return nm;
            }

            public void setNm(String nm) {
                this.nm = nm;
            }

            public String getScm() {
                return scm;
            }

            public void setScm(String scm) {
                this.scm = scm;
            }

            public int getId() {
                return id;
            }

            public void setId(int id) {
                this.id = id;
            }

            public String getTime() {
                return time;
            }

            public void setTime(String time) {
                this.time = time;
            }
        }
    }
}
