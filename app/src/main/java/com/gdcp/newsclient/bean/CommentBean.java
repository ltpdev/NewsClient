package com.gdcp.newsclient.bean;

import java.util.List;

/**
 * Created by asus- on 2017/8/15.
 */

public class CommentBean {

    /**
     * expires : 300
     */

    private ControlBean control;
    /**
     * control : {"expires":300}
     * status : 0
     * data : {"CommentResponseModel":{"total":1057971,"cmts":[{"vipInfo":"","nickName":"cpE636335274","score":3.5,"userId":705321863,"time":"2017-08-15 09:04","nick":"cpE636335274","approve":0,"oppose":0,"reply":0,"avatarurl":"http://p0.meituan.net/movie/__40465654__9539763.png","id":118891567,"content":"情节连贯，英雄突出，达康书记演的不错\u2026\u2026"},{"vipInfo":"","nickName":"毒￡丽＃庵","score":5,"userId":449174170,"time":"2017-08-15 09:04","nick":"毒￡丽＃庵","approve":0,"oppose":0,"reply":0,"avatarurl":"https://img.meituan.net/avatar/9253cc7d3f2f4d6b9f9d56099e0b606310828.jpg","id":118891564,"content":"一日为战狼 终生是战狼"},{"vipInfo":"","nickName":"JtV165961860","score":5,"userId":89243682,"time":"2017-08-15 09:03","nick":"JtV165961860","approve":0,"oppose":0,"reply":0,"avatarurl":"http://p0.meituan.net/movie/__40465654__9539763.png","id":118891552,"content":"好看，激动人心，带儿子看了两遍"},{"vipInfo":"","nickName":"Shengfuren520","score":5,"userId":317755552,"time":"2017-08-15 09:03","nick":"Shengfuren520","approve":0,"oppose":0,"reply":0,"avatarurl":"https://img.meituan.net/avatar/88e285bc9ef87d8387b8af41b6bcc4fa6291.jpg","id":118891525,"content":"非常不错！支持吴京！"},{"vipInfo":"","nickName":"umE926116043","score":5,"userId":677522649,"time":"2017-08-15 09:03","nick":"umE926116043","approve":0,"oppose":0,"reply":0,"avatarurl":"http://p0.meituan.net/movie/__40465654__9539763.png","id":118891524,"content":"动作阳刚紧凑"}]},"hasNext":true,"movieid":344264,"offset":10,"limit":5}
     */

    private int status;
    /**
     * CommentResponseModel : {"total":1057971,"cmts":[{"vipInfo":"","nickName":"cpE636335274","score":3.5,"userId":705321863,"time":"2017-08-15 09:04","nick":"cpE636335274","approve":0,"oppose":0,"reply":0,"avatarurl":"http://p0.meituan.net/movie/__40465654__9539763.png","id":118891567,"content":"情节连贯，英雄突出，达康书记演的不错\u2026\u2026"},{"vipInfo":"","nickName":"毒￡丽＃庵","score":5,"userId":449174170,"time":"2017-08-15 09:04","nick":"毒￡丽＃庵","approve":0,"oppose":0,"reply":0,"avatarurl":"https://img.meituan.net/avatar/9253cc7d3f2f4d6b9f9d56099e0b606310828.jpg","id":118891564,"content":"一日为战狼 终生是战狼"},{"vipInfo":"","nickName":"JtV165961860","score":5,"userId":89243682,"time":"2017-08-15 09:03","nick":"JtV165961860","approve":0,"oppose":0,"reply":0,"avatarurl":"http://p0.meituan.net/movie/__40465654__9539763.png","id":118891552,"content":"好看，激动人心，带儿子看了两遍"},{"vipInfo":"","nickName":"Shengfuren520","score":5,"userId":317755552,"time":"2017-08-15 09:03","nick":"Shengfuren520","approve":0,"oppose":0,"reply":0,"avatarurl":"https://img.meituan.net/avatar/88e285bc9ef87d8387b8af41b6bcc4fa6291.jpg","id":118891525,"content":"非常不错！支持吴京！"},{"vipInfo":"","nickName":"umE926116043","score":5,"userId":677522649,"time":"2017-08-15 09:03","nick":"umE926116043","approve":0,"oppose":0,"reply":0,"avatarurl":"http://p0.meituan.net/movie/__40465654__9539763.png","id":118891524,"content":"动作阳刚紧凑"}]}
     * hasNext : true
     * movieid : 344264
     * offset : 10
     * limit : 5
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
        /**
         * total : 1057971
         * cmts : [{"vipInfo":"","nickName":"cpE636335274","score":3.5,"userId":705321863,"time":"2017-08-15 09:04","nick":"cpE636335274","approve":0,"oppose":0,"reply":0,"avatarurl":"http://p0.meituan.net/movie/__40465654__9539763.png","id":118891567,"content":"情节连贯，英雄突出，达康书记演的不错\u2026\u2026"},{"vipInfo":"","nickName":"毒￡丽＃庵","score":5,"userId":449174170,"time":"2017-08-15 09:04","nick":"毒￡丽＃庵","approve":0,"oppose":0,"reply":0,"avatarurl":"https://img.meituan.net/avatar/9253cc7d3f2f4d6b9f9d56099e0b606310828.jpg","id":118891564,"content":"一日为战狼 终生是战狼"},{"vipInfo":"","nickName":"JtV165961860","score":5,"userId":89243682,"time":"2017-08-15 09:03","nick":"JtV165961860","approve":0,"oppose":0,"reply":0,"avatarurl":"http://p0.meituan.net/movie/__40465654__9539763.png","id":118891552,"content":"好看，激动人心，带儿子看了两遍"},{"vipInfo":"","nickName":"Shengfuren520","score":5,"userId":317755552,"time":"2017-08-15 09:03","nick":"Shengfuren520","approve":0,"oppose":0,"reply":0,"avatarurl":"https://img.meituan.net/avatar/88e285bc9ef87d8387b8af41b6bcc4fa6291.jpg","id":118891525,"content":"非常不错！支持吴京！"},{"vipInfo":"","nickName":"umE926116043","score":5,"userId":677522649,"time":"2017-08-15 09:03","nick":"umE926116043","approve":0,"oppose":0,"reply":0,"avatarurl":"http://p0.meituan.net/movie/__40465654__9539763.png","id":118891524,"content":"动作阳刚紧凑"}]
         */

        private CommentResponseModelBean CommentResponseModel;
        private boolean hasNext;
        private int movieid;
        private int offset;
        private int limit;

        public CommentResponseModelBean getCommentResponseModel() {
            return CommentResponseModel;
        }

        public void setCommentResponseModel(CommentResponseModelBean CommentResponseModel) {
            this.CommentResponseModel = CommentResponseModel;
        }

        public boolean isHasNext() {
            return hasNext;
        }

        public void setHasNext(boolean hasNext) {
            this.hasNext = hasNext;
        }

        public int getMovieid() {
            return movieid;
        }

        public void setMovieid(int movieid) {
            this.movieid = movieid;
        }

        public int getOffset() {
            return offset;
        }

        public void setOffset(int offset) {
            this.offset = offset;
        }

        public int getLimit() {
            return limit;
        }

        public void setLimit(int limit) {
            this.limit = limit;
        }

        public static class CommentResponseModelBean {
            private int total;
            /**
             * vipInfo :
             * nickName : cpE636335274
             * score : 3.5
             * userId : 705321863
             * time : 2017-08-15 09:04
             * nick : cpE636335274
             * approve : 0
             * oppose : 0
             * reply : 0
             * avatarurl : http://p0.meituan.net/movie/__40465654__9539763.png
             * id : 118891567
             * content : 情节连贯，英雄突出，达康书记演的不错……
             */

            private List<CmtsBean> cmts;

            public int getTotal() {
                return total;
            }

            public void setTotal(int total) {
                this.total = total;
            }

            public List<CmtsBean> getCmts() {
                return cmts;
            }

            public void setCmts(List<CmtsBean> cmts) {
                this.cmts = cmts;
            }

            public static class CmtsBean {
                private String vipInfo;
                private String nickName;
                private double score;
                private int userId;
                private String time;
                private String nick;
                private int approve;
                private int oppose;
                private int reply;
                private String avatarurl;
                private int id;
                private String content;

                public String getVipInfo() {
                    return vipInfo;
                }

                public void setVipInfo(String vipInfo) {
                    this.vipInfo = vipInfo;
                }

                public String getNickName() {
                    return nickName;
                }

                public void setNickName(String nickName) {
                    this.nickName = nickName;
                }

                public double getScore() {
                    return score;
                }

                public void setScore(double score) {
                    this.score = score;
                }

                public int getUserId() {
                    return userId;
                }

                public void setUserId(int userId) {
                    this.userId = userId;
                }

                public String getTime() {
                    return time;
                }

                public void setTime(String time) {
                    this.time = time;
                }

                public String getNick() {
                    return nick;
                }

                public void setNick(String nick) {
                    this.nick = nick;
                }

                public int getApprove() {
                    return approve;
                }

                public void setApprove(int approve) {
                    this.approve = approve;
                }

                public int getOppose() {
                    return oppose;
                }

                public void setOppose(int oppose) {
                    this.oppose = oppose;
                }

                public int getReply() {
                    return reply;
                }

                public void setReply(int reply) {
                    this.reply = reply;
                }

                public String getAvatarurl() {
                    return avatarurl;
                }

                public void setAvatarurl(String avatarurl) {
                    this.avatarurl = avatarurl;
                }

                public int getId() {
                    return id;
                }

                public void setId(int id) {
                    this.id = id;
                }

                public String getContent() {
                    return content;
                }

                public void setContent(String content) {
                    this.content = content;
                }
            }
        }
    }
}
